package com.example.callguardian.data.repository

import com.example.callguardian.ai.AiRiskScorer
import com.example.callguardian.data.db.PhoneInteractionDao
import com.example.callguardian.data.db.PhoneInteractionEntity
import com.example.callguardian.data.db.PhoneProfileDao
import com.example.callguardian.data.db.PhoneProfileEntity
import com.example.callguardian.lookup.ReverseLookupManager
import com.example.callguardian.model.AiAssessment
import com.example.callguardian.model.BlockMode
import com.example.callguardian.model.ContactInfo
import com.example.callguardian.model.InteractionDirection
import com.example.callguardian.model.InteractionType
import com.example.callguardian.model.LookupOutcome
import com.example.callguardian.model.LookupResult
import com.example.callguardian.service.ContactChange
import com.example.callguardian.service.ContactInfoField
import com.example.callguardian.service.ContactLookupService
import com.example.callguardian.service.ContactSyncResult
import com.example.callguardian.service.ContactSyncService
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
    private val contactSyncService: ContactSyncService,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun performLookup(
        phoneNumber: String,
        type: InteractionType,
        direction: InteractionDirection,
        messageBody: String? = null
    ): LookupOutcome = withContext(ioDispatcher) {
        val normalized = phoneNumber.filter { it.isDigit() || it == '+' }
        val existing = phoneProfileDao.getByNumber(normalized)
        val contactInfo = contactLookupService.lookupContact(normalized)
        val lookupResult = reverseLookupManager.lookup(normalized)
        val aiAssessment = aiRiskScorer.evaluate(normalized, messageBody, lookupResult)

        // Merge contact info with lookup result for better display name
        val mergedLookupResult = mergeContactInfoWithLookupResult(lookupResult, contactInfo)

        val profile = existing
            ?.merge(mergedLookupResult, normalized, aiAssessment, contactInfo)
            ?: mergedLookupResult?.toEntity(normalized, aiAssessment, contactInfo)

        if (profile != null) {
            phoneProfileDao.upsert(profile)
        }

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

        LookupOutcome(
            lookupResult = mergedLookupResult,
            aiAssessment = aiAssessment,
            contactInfo = contactInfo
        )
    }

    suspend fun updateBlockMode(phoneNumber: String, blockMode: BlockMode) {
        withContext(ioDispatcher) {
            phoneProfileDao.updateBlockMode(phoneNumber, blockMode.name)
        }
    }

    suspend fun updateContactInfo(phoneNumber: String, contactId: Long?, isExistingContact: Boolean) {
        withContext(ioDispatcher) {
            phoneProfileDao.updateContactInfo(phoneNumber, contactId, isExistingContact)
        }
    }

    /**
     * Analyzes if a contact sync is needed and returns sync recommendations
     */
    suspend fun analyzeContactSync(phoneNumber: String): ContactSyncResult {
        val lookupOutcome = performLookup(
            phoneNumber = phoneNumber,
            type = InteractionType.CALL,
            direction = InteractionDirection.INCOMING
        )

        return contactSyncService.analyzeContactSync(phoneNumber, lookupOutcome)
    }

    /**
     * Applies approved contact sync changes
     */
    suspend fun applyContactSyncChanges(
        contactInfo: ContactInfo,
        approvedChanges: List<ContactChange>,
        newInfo: List<ContactInfoField>
    ): Boolean {
        return contactSyncService.applyContactChanges(contactInfo, approvedChanges, newInfo)
    }

    suspend fun getProfile(phoneNumber: String): PhoneProfileEntity? = withContext(ioDispatcher) {
        phoneProfileDao.getByNumber(phoneNumber.filter { it.isDigit() || it == '+' })
    }

    fun observeProfiles(): Flow<List<PhoneProfileEntity>> = phoneProfileDao.observeAll()

    fun observeRecentInteractions(limit: Int = 50) = interactionDao.observeRecent(limit)

    suspend fun isExistingContact(phoneNumber: String): Boolean = withContext(ioDispatcher) {
        contactLookupService.lookupContact(phoneNumber.filter { it.isDigit() || it == '+' })?.existsInContacts == true
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
            displayName = contactInfo.displayName.takeIf { it.isNotBlank() } ?: lookupResult.displayName,
            tags = lookupResult.tags + "Existing Contact"
        )
    }
}
