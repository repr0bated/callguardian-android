package com.example.callguardian.util

import com.example.callguardian.ai.AiAssessment
import com.example.callguardian.data.db.PhoneInteractionEntity
import com.example.callguardian.data.db.PhoneProfileEntity
import com.example.callguardian.model.BlockMode
import com.example.callguardian.model.ContactInfo
import com.example.callguardian.model.InteractionDirection
import com.example.callguardian.model.InteractionType
import com.example.callguardian.model.LookupResult
import com.example.callguardian.service.ContactChange
import com.example.callguardian.service.ContactInfoField
import java.time.Instant

object TestDataFactory {

    fun createLookupResult(
        phoneNumber: String = "+15551234567",
        displayName: String = "Test Contact",
        carrier: String = "Test Carrier",
        region: String = "Test Region",
        lineType: String = "Mobile",
        spamScore: Int = 25,
        tags: List<String> = listOf("Test Tag"),
        summary: String = "Test summary"
    ): LookupResult = LookupResult(
        phoneNumber = phoneNumber,
        displayName = displayName,
        carrier = carrier,
        region = region,
        lineType = lineType,
        spamScore = spamScore,
        tags = tags,
        summary = summary
    )

    fun createAiAssessment(
        label: String = "Low Risk",
        confidencePercent: Int = 85,
        explanation: String = "Test explanation"
    ): AiAssessment = AiAssessment(
        label = label,
        confidencePercent = confidencePercent,
        explanation = explanation
    )

    fun createContactInfo(
        phoneNumber: String = "+15551234567",
        displayName: String = "Test Contact",
        contactId: Long = 12345L,
        existsInContacts: Boolean = true,
        photoUri: String? = null
    ): ContactInfo = ContactInfo(
        phoneNumber = phoneNumber,
        displayName = displayName,
        contactId = contactId,
        existsInContacts = existsInContacts,
        photoUri = photoUri
    )

    fun createPhoneProfileEntity(
        phoneNumber: String = "+15551234567",
        displayName: String = "Test Contact",
        carrier: String = "Test Carrier",
        region: String = "Test Region",
        lineType: String = "Mobile",
        spamScore: Int = 25,
        tags: List<String> = listOf("Test Tag"),
        blockMode: BlockMode = BlockMode.NONE,
        notes: String? = null,
        contactId: Long? = 12345L,
        isExistingContact: Boolean = true,
        lastLookupAt: Instant = Instant.now()
    ): PhoneProfileEntity = PhoneProfileEntity(
        phoneNumber = phoneNumber,
        displayName = displayName,
        carrier = carrier,
        region = region,
        lineType = lineType,
        spamScore = spamScore,
        tags = tags,
        blockMode = blockMode,
        notes = notes,
        contactId = contactId,
        isExistingContact = isExistingContact,
        lastLookupAt = lastLookupAt
    )

    fun createPhoneInteractionEntity(
        phoneNumber: String = "+15551234567",
        type: InteractionType = InteractionType.CALL,
        direction: InteractionDirection = InteractionDirection.INCOMING,
        timestamp: Instant = Instant.now(),
        status: String = "ALLOWED",
        messageBody: String? = null,
        lookupSummary: String? = "Test summary"
    ): PhoneInteractionEntity = PhoneInteractionEntity(
        phoneNumber = phoneNumber,
        type = type,
        direction = direction,
        timestamp = timestamp,
        status = status,
        messageBody = messageBody,
        lookupSummary = lookupSummary
    )

    fun createContactChange(
        field: ContactInfoField = ContactInfoField.DISPLAY_NAME,
        oldValue: String = "Old Name",
        newValue: String = "New Name"
    ): ContactChange = ContactChange(
        field = field,
        oldValue = oldValue,
        newValue = newValue
    )

    fun createContactInfoField(
        field: ContactInfoField = ContactInfoField.DISPLAY_NAME,
        value: String = "Test Value"
    ): ContactInfoField = ContactInfoField(
        field = field,
        value = value
    )

    // Helper methods for creating test data variations
    fun createSpamLookupResult(): LookupResult = createLookupResult(
        displayName = "Spam Caller",
        spamScore = 90,
        tags = listOf("Spam", "Telemarketer")
    )

    fun createHighRiskAiAssessment(): AiAssessment = createAiAssessment(
        label = "High Risk",
        confidencePercent = 95,
        explanation = "High probability of spam based on message content and number analysis"
    )

    fun createBlockedProfile(): PhoneProfileEntity = createPhoneProfileEntity(
        blockMode = BlockMode.BLOCK_ALL,
        spamScore = 85,
        tags = listOf("Spam", "High Risk")
    )

    fun createIncomingCallInteraction(): PhoneInteractionEntity = createPhoneInteractionEntity(
        type = InteractionType.CALL,
        direction = InteractionDirection.INCOMING
    )

    fun createBlockedSmsInteraction(): PhoneInteractionEntity = createPhoneInteractionEntity(
        type = InteractionType.SMS,
        direction = InteractionDirection.INCOMING,
        status = "BLOCKED",
        messageBody = "This is a spam message"
    )
}