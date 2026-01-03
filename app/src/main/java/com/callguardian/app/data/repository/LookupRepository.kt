package com.callguardian.app.data.repository

import com.callguardian.app.ai.AiRiskScorer
import com.callguardian.app.data.db.PhoneInteractionDao
import com.callguardian.app.data.db.PhoneInteractionEntity
import com.callguardian.app.data.db.PhoneProfileDao
import com.callguardian.app.data.db.PhoneProfileEntity
import com.callguardian.app.lookup.ReverseLookupManager
import com.callguardian.app.model.AiAssessment
import com.callguardian.app.model.BlockMode
import com.callguardian.app.model.InteractionDirection
import com.callguardian.app.model.InteractionType
import com.callguardian.app.model.LookupOutcome
import com.callguardian.app.model.LookupResult
import com.callguardian.app.service.ContactChange
import com.callguardian.app.service.ContactInfo
import com.callguardian.app.service.ContactInfoField
import com.callguardian.app.service.ContactLookupService
import com.callguardian.app.service.ContactSyncResult
import com.callguardian.app.util.database.DatabaseManager
import com.callguardian.app.util.exceptions.CacheException
import com.callguardian.app.util.exceptions.DatabaseException
import com.callguardian.app.util.exceptions.ExceptionFactory
import com.callguardian.app.util.exceptions.LookupException
import com.callguardian.app.util.handling.ErrorHandling
import com.callguardian.app.util.logging.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LookupRepository @Inject constructor(
    private val reverseLookupManager: ReverseLookupManager,
    private val phoneProfileDao: PhoneProfileDao,
    private val interactionDao: PhoneInteractionDao,
    private val aiRiskScorer: AiRiskScorer,
    private val contactLookupService: ContactLookupService,
    private val databaseManager: DatabaseManager,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun performLookup(
        phoneNumber: String,
        type: InteractionType,
        direction: InteractionDirection,
        messageBody: String? = null
    ): LookupOutcome = withContext(ioDispatcher) {
        var contactInfo: ContactInfo? = null
        try {
            val normalized = phoneNumber.filter { it.isDigit() || it == '+' }
            
            // Use DatabaseManager for database operations with graceful degradation
            val existing = databaseManager.safeQuery("getByNumber") {
                phoneProfileDao.getByNumber(normalized)
            }
            
            contactInfo = contactLookupService.lookupContact(normalized)
            val lookupResult = reverseLookupManager.lookup(normalized)
            val aiAssessment = aiRiskScorer.evaluate(normalized, messageBody, lookupResult)

            // Merge contact info with lookup result for better display name
            val mergedLookupResult = mergeContactInfoWithLookupResult(lookupResult, contactInfo)

            val profile = existing
                ?.merge(mergedLookupResult, normalized, aiAssessment, contactInfo)
                ?: mergedLookupResult?.toEntity(normalized, aiAssessment, contactInfo)

            if (profile != null) {
                databaseManager.safeTransaction("upsertProfile") {
                    phoneProfileDao.upsert(profile)
                }
            }

            databaseManager.safeTransaction("insertInteraction") {
                interactionDao.insert(
                    PhoneInteractionEntity(
                        phoneNumber = normalized,
                        type = type,
                        direction = direction,
                        timestamp = Instant.now(),
                        status = when {
                            profile?.shouldBlockCalls() == true && type == InteractionType.CALL -> "BLOCKED"
                            profile?.shouldBlockMessages() == true && type == InteractionType.SMS -> "BLOCKED"
                            else -> "ALLOWED"
                        },
                        messageBody = messageBody,
                        lookupSummary = buildSummary(mergedLookupResult, profile, aiAssessment, contactInfo)
                    )
                )
            }

            LookupOutcome(
                lookupResult = mergedLookupResult,
                contactInfo = contactInfo
            )
        } catch (e: LookupException) {
            Logger.e(e, "Lookup failed for phone number: $phoneNumber")
            throw e
        } catch (e: DatabaseException) {
            Logger.e(e, "Database error during lookup for phone number: $phoneNumber")
            // Continue with degraded functionality
            return@withContext LookupOutcome(
                lookupResult = null,
                contactInfo = null
            )
        } catch (e: Exception) {
            Logger.e(e, "Unexpected error during lookup for phone number: $phoneNumber")
            throw ExceptionFactory.createLookupException("Lookup repository operation failed", e)
        }
    }

    suspend fun updateBlockMode(phoneNumber: String, blockMode: BlockMode) {
        withContext(ioDispatcher) {
            try {
                databaseManager.safeTransaction("updateBlockMode") {
                    phoneProfileDao.updateBlockMode(phoneNumber, blockMode.name)
                }
            } catch (e: DatabaseException) {
                Logger.e(e, "Failed to update block mode for phone number: $phoneNumber")
                throw e
            }
        }
    }

    suspend fun updateContactInfo(phoneNumber: String, contactId: Long?, isExistingContact: Boolean) {
        withContext(ioDispatcher) {
            try {
                databaseManager.safeTransaction("updateContactInfo") {
                    phoneProfileDao.updateContactInfo(phoneNumber, contactId, isExistingContact)
                }
            } catch (e: DatabaseException) {
                Logger.e(e, "Failed to update contact info for phone number: $phoneNumber")
                throw e
            }
        }
    }



    suspend fun getProfile(phoneNumber: String): PhoneProfileEntity? = withContext(ioDispatcher) {
        try {
            databaseManager.safeQuery("getProfile") {
                phoneProfileDao.getByNumber(phoneNumber.filter { it.isDigit() || it == '+' })
            }
        } catch (e: DatabaseException) {
            Logger.e(e, "Failed to get profile for phone number: $phoneNumber")
            return@withContext null
        }
    }

    fun observeProfiles(): Flow<List<PhoneProfileEntity>> = phoneProfileDao.observeAll()

    fun observeRecentInteractions(limit: Int = 50) = interactionDao.observeRecent(limit)

    suspend fun isExistingContact(phoneNumber: String): Boolean = withContext(ioDispatcher) {
        try {
            contactLookupService.lookupContact(phoneNumber.filter { it.isDigit() || it == '+' })?.existsInContacts == true
        } catch (e: ServiceException) {
            Logger.e(e, "Failed to check if contact exists for phone number: $phoneNumber")
            return@withContext false
        }
    }

    private fun LookupResult.toEntity(
        normalizedNumber: String,
        aiAssessment: AiAssessment?,
        contactInfo: ContactInfo?
    ) = PhoneProfileEntity(
        phoneNumber = normalizedNumber,
        displayName = displayName,
        carrier = carrier,
        region = region,
        lineType = lineType,
        lastLookupAt = Instant.now(),
        spamScore = spamScore,
        tags = tags.withAiTag(aiAssessment).withContactTag(contactInfo),
        blockMode = BlockMode.NONE,
        notes = null,
        contactId = contactInfo?.contactId,
        isExistingContact = contactInfo?.existsInContacts ?: false
    )

    private fun PhoneProfileEntity.merge(
        lookupResult: LookupResult?,
        normalizedNumber: String,
        aiAssessment: AiAssessment?,
        contactInfo: ContactInfo?
    ): PhoneProfileEntity = copy(
        phoneNumber = normalizedNumber,
        displayName = lookupResult?.displayName ?: displayName,
        carrier = lookupResult?.carrier ?: carrier,
        region = lookupResult?.region ?: region,
        lineType = lookupResult?.lineType ?: lineType,
        spamScore = lookupResult?.spamScore ?: spamScore,
        tags = lookupResult?.tags.withAiTag(aiAssessment).withContactTag(contactInfo)
            .takeUnless { it.isEmpty() }
            ?: tags.withAiTag(aiAssessment).withContactTag(contactInfo),
        lastLookupAt = Instant.now(),
        contactId = contactInfo?.contactId ?: contactId,
        isExistingContact = contactInfo?.existsInContacts ?: isExistingContact
    )

    private fun List<String>?.withAiTag(aiAssessment: AiAssessment?): List<String> {
        val aiTag = aiAssessment?.let {
            "AI Risk: ${it.label} (${it.confidencePercent}%)"
        }
        return when {
            aiTag == null -> this ?: emptyList()
            this == null || isEmpty() -> listOf(aiTag)
            contains(aiTag) -> this
            else -> this + aiTag
        }
    }

    private fun List<String>?.withContactTag(contactInfo: ContactInfo?): List<String> {
        val contactTag = contactInfo?.takeIf { it.existsInContacts }?.let { "Existing Contact" }
        return when {
            contactTag == null -> this ?: emptyList()
            this == null || isEmpty() -> listOf(contactTag)
            contains(contactTag) -> this
            else -> this + contactTag
        }
    }

    private fun buildSummary(
        lookupResult: LookupResult?,
        profile: PhoneProfileEntity?,
        aiAssessment: AiAssessment?,
        contactInfo: ContactInfo?
    ): String? {
        val base = lookupResult?.summary ?: profile?.displayName ?: contactInfo?.displayName
        val aiSummary = aiAssessment?.let { "AI: ${it.label} (${it.confidencePercent}%)" }
        val contactTag = contactInfo?.takeIf { it.existsInContacts }?.let { "📞 Contact" }
        return listOfNotNull(base, aiSummary, contactTag).takeIf { it.isNotEmpty() }?.joinToString(separator = " • ")
    }

    private fun mergeContactInfoWithLookupResult(
        lookupResult: LookupResult?,
        contactInfo: ContactInfo?
    ): LookupResult? {
        if (lookupResult == null) return null
        if (contactInfo?.existsInContacts != true) return lookupResult

        // Prioritize contact name over lookup result name if contact exists
        return lookupResult.copy(
            displayName = contactInfo.displayName?.takeIf { it.isNotBlank() } ?: lookupResult.displayName,
            tags = lookupResult.tags + "Existing Contact"
        )
    }
}
