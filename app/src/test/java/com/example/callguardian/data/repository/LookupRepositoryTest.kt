package com.example.callguardian.data.repository

import com.example.callguardian.ai.AiAssessment
import com.example.callguardian.data.db.PhoneInteractionDao
import com.example.callguardian.data.db.PhoneInteractionEntity
import com.example.callguardian.data.db.PhoneProfileDao
import com.example.callguardian.data.db.PhoneProfileEntity
import com.example.callguardian.lookup.ReverseLookupManager
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
import com.example.callguardian.util.TestDataFactory
import com.example.callguardian.util.TestUtil
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LookupRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: LookupRepository
    
    // Mock dependencies
    private val mockReverseLookupManager = mockk<ReverseLookupManager>(relaxed = true)
    private val mockPhoneProfileDao = mockk<PhoneProfileDao>(relaxed = true)
    private val mockInteractionDao = mockk<PhoneInteractionDao>(relaxed = true)
    private val mockAiRiskScorer = mockk<com.example.callguardian.ai.AiRiskScorer>(relaxed = true)
    private val mockContactLookupService = mockk<ContactLookupService>(relaxed = true)
    private val mockContactSyncService = mockk<ContactSyncService>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        repository = LookupRepository(
            reverseLookupManager = mockReverseLookupManager,
            phoneProfileDao = mockPhoneProfileDao,
            interactionDao = mockInteractionDao,
            aiRiskScorer = mockAiRiskScorer,
            contactLookupService = mockContactLookupService,
            contactSyncService = mockContactSyncService,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `performLookup creates new profile when no existing profile found`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val lookupResult = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        val contactInfo = TestDataFactory.createContactInfo(phoneNumber = phoneNumber)
        val aiAssessment = TestDataFactory.createAiAssessment()
        
        coEvery { mockPhoneProfileDao.getByNumber(any()) } returns null
        coEvery { mockContactLookupService.lookupContact(any()) } returns contactInfo
        coEvery { mockReverseLookupManager.lookup(any()) } returns lookupResult
        coEvery { mockAiRiskScorer.evaluate(any(), any(), any()) } returns aiAssessment
        coEvery { mockPhoneProfileDao.upsert(any()) } returns Unit
        coEvery { mockInteractionDao.insert(any()) } returns Unit

        // When
        val result = repository.performLookup(
            phoneNumber = phoneNumber,
            type = InteractionType.CALL,
            direction = InteractionDirection.INCOMING
        )

        // Then
        assertNotNull(result)
        assertEquals(lookupResult, result.lookupResult)
        assertEquals(aiAssessment, result.aiAssessment)
        assertEquals(contactInfo, result.contactInfo)
        
        coVerify { mockPhoneProfileDao.upsert(any()) }
        coVerify { mockInteractionDao.insert(any()) }
    }

    @Test
    fun `performLookup updates existing profile when found`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val existingProfile = TestDataFactory.createPhoneProfileEntity(phoneNumber = phoneNumber)
        val lookupResult = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        val contactInfo = TestDataFactory.createContactInfo(phoneNumber = phoneNumber)
        val aiAssessment = TestDataFactory.createAiAssessment()
        
        coEvery { mockPhoneProfileDao.getByNumber(any()) } returns existingProfile
        coEvery { mockContactLookupService.lookupContact(any()) } returns contactInfo
        coEvery { mockReverseLookupManager.lookup(any()) } returns lookupResult
        coEvery { mockAiRiskScorer.evaluate(any(), any(), any()) } returns aiAssessment
        coEvery { mockPhoneProfileDao.upsert(any()) } returns Unit
        coEvery { mockInteractionDao.insert(any()) } returns Unit

        // When
        val result = repository.performLookup(
            phoneNumber = phoneNumber,
            type = InteractionType.CALL,
            direction = InteractionDirection.INCOMING
        )

        // Then
        assertNotNull(result)
        assertEquals(lookupResult, result.lookupResult)
        assertEquals(aiAssessment, result.aiAssessment)
        assertEquals(contactInfo, result.contactInfo)
        
        coVerify { mockPhoneProfileDao.upsert(any()) }
        coVerify { mockInteractionDao.insert(any()) }
    }

    @Test
    fun `performLookup handles SMS with message body`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val messageBody = "This is a test message"
        val lookupResult = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        val contactInfo = TestDataFactory.createContactInfo(phoneNumber = phoneNumber)
        val aiAssessment = TestDataFactory.createAiAssessment()
        
        coEvery { mockPhoneProfileDao.getByNumber(any()) } returns null
        coEvery { mockContactLookupService.lookupContact(any()) } returns contactInfo
        coEvery { mockReverseLookupManager.lookup(any()) } returns lookupResult
        coEvery { mockAiRiskScorer.evaluate(any(), any(), any()) } returns aiAssessment
        coEvery { mockPhoneProfileDao.upsert(any()) } returns Unit
        coEvery { mockInteractionDao.insert(any()) } returns Unit

        // When
        val result = repository.performLookup(
            phoneNumber = phoneNumber,
            type = InteractionType.SMS,
            direction = InteractionDirection.INCOMING,
            messageBody = messageBody
        )

        // Then
        assertNotNull(result)
        assertEquals(lookupResult, result.lookupResult)
        assertEquals(aiAssessment, result.aiAssessment)
        assertEquals(contactInfo, result.contactInfo)
        
        coVerify { mockAiRiskScorer.evaluate(phoneNumber, messageBody, lookupResult) }
    }

    @Test
    fun `updateBlockMode updates profile block mode`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val blockMode = BlockMode.BLOCK_ALL
        
        coEvery { mockPhoneProfileDao.updateBlockMode(any(), any()) } returns Unit

        // When
        repository.updateBlockMode(phoneNumber, blockMode)

        // Then
        coVerify { mockPhoneProfileDao.updateBlockMode(phoneNumber, blockMode.name) }
    }

    @Test
    fun `updateContactInfo updates profile contact information`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val contactId = 12345L
        val isExistingContact = true
        
        coEvery { mockPhoneProfileDao.updateContactInfo(any(), any(), any()) } returns Unit

        // When
        repository.updateContactInfo(phoneNumber, contactId, isExistingContact)

        // Then
        coVerify { mockPhoneProfileDao.updateContactInfo(phoneNumber, contactId, isExistingContact) }
    }

    @Test
    fun `analyzeContactSync returns sync result`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val lookupOutcome = LookupOutcome(
            lookupResult = TestDataFactory.createLookupResult(phoneNumber = phoneNumber),
            aiAssessment = TestDataFactory.createAiAssessment(),
            contactInfo = TestDataFactory.createContactInfo(phoneNumber = phoneNumber)
        )
        val syncResult = ContactSyncResult(
            shouldSync = true,
            changes = listOf(),
            newInfo = listOf()
        )
        
        coEvery { mockReverseLookupManager.lookup(any()) } returns lookupOutcome.lookupResult
        coEvery { mockAiRiskScorer.evaluate(any(), any(), any()) } returns lookupOutcome.aiAssessment
        coEvery { mockContactLookupService.lookupContact(any()) } returns lookupOutcome.contactInfo
        coEvery { mockContactSyncService.analyzeContactSync(any(), any()) } returns syncResult

        // When
        val result = repository.analyzeContactSync(phoneNumber)

        // Then
        assertEquals(syncResult, result)
    }

    @Test
    fun `applyContactSyncChanges delegates to contact sync service`() = runTest {
        // Given
        val contactInfo = TestDataFactory.createContactInfo()
        val approvedChanges = listOf(
            ContactChange(
                field = ContactInfoField.DISPLAY_NAME,
                oldValue = "Old Name",
                newValue = "New Name"
            )
        )
        val newInfo = listOf(
            ContactInfoField(
                field = ContactInfoField.DISPLAY_NAME,
                value = "Updated Name"
            )
        )
        
        coEvery { mockContactSyncService.applyContactChanges(any(), any(), any()) } returns true

        // When
        val result = repository.applyContactSyncChanges(contactInfo, approvedChanges, newInfo)

        // Then
        assertTrue(result)
        coVerify { mockContactSyncService.applyContactChanges(contactInfo, approvedChanges, newInfo) }
    }

    @Test
    fun `getProfile returns profile from DAO`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val expectedProfile = TestDataFactory.createPhoneProfileEntity(phoneNumber = phoneNumber)
        
        coEvery { mockPhoneProfileDao.getByNumber(any()) } returns expectedProfile

        // When
        val result = repository.getProfile(phoneNumber)

        // Then
        assertEquals(expectedProfile, result)
    }

    @Test
    fun `observeProfiles delegates to DAO`() {
        // Given
        val expectedProfiles = listOf(
            TestDataFactory.createPhoneProfileEntity(phoneNumber = "+15551234567"),
            TestDataFactory.createPhoneProfileEntity(phoneNumber = "+15559876543")
        )
        
        every { mockPhoneProfileDao.observeAll() } returns flowOf(expectedProfiles)

        // When
        val resultFlow = repository.observeProfiles()

        // Then
        // Note: In a real test, you would collect the flow and verify the values
        // For this example, we're just verifying the method is called
        verify { mockPhoneProfileDao.observeAll() }
    }

    @Test
    fun `observeRecentInteractions delegates to DAO`() {
        // Given
        val limit = 50
        val expectedInteractions = listOf(
            TestDataFactory.createPhoneInteractionEntity(phoneNumber = "+15551234567"),
            TestDataFactory.createPhoneInteractionEntity(phoneNumber = "+15559876543")
        )
        
        every { mockInteractionDao.observeRecent(limit) } returns flowOf(expectedInteractions)

        // When
        val resultFlow = repository.observeRecentInteractions(limit)

        // Then
        verify { mockInteractionDao.observeRecent(limit) }
    }

    @Test
    fun `isExistingContact returns true when contact exists`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val contactInfo = TestDataFactory.createContactInfo(
            phoneNumber = phoneNumber,
            existsInContacts = true
        )
        
        coEvery { mockContactLookupService.lookupContact(any()) } returns contactInfo

        // When
        val result = repository.isExistingContact(phoneNumber)

        // Then
        assertTrue(result)
    }

    @Test
    fun `isExistingContact returns false when contact does not exist`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val contactInfo = TestDataFactory.createContactInfo(
            phoneNumber = phoneNumber,
            existsInContacts = false
        )
        
        coEvery { mockContactLookupService.lookupContact(any()) } returns contactInfo

        // When
        val result = repository.isExistingContact(phoneNumber)

        // Then
        assertTrue(!result)
    }

    @Test
    fun `isExistingContact returns false when contact info is null`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        
        coEvery { mockContactLookupService.lookupContact(any()) } returns null

        // When
        val result = repository.isExistingContact(phoneNumber)

        // Then
        assertTrue(!result)
    }
}