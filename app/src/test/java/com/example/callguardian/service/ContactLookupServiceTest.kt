package com.example.callguardian.service

import com.example.callguardian.model.ContactInfo
import com.example.callguardian.util.TestDataFactory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class ContactLookupServiceTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var service: ContactLookupService
    
    // Mock dependencies
    private val mockContext = mockk<android.content.Context>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        service = ContactLookupService(mockContext)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `lookupContact returns contact info when contact exists`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val contactInfo = TestDataFactory.createContactInfo(
            phoneNumber = phoneNumber,
            existsInContacts = true
        )
        
        coEvery { service.lookupContact(phoneNumber) } returns contactInfo

        // When
        val result = service.lookupContact(phoneNumber)

        // Then
        assertNotNull(result)
        assertEquals(phoneNumber, result.phoneNumber)
        assertEquals(true, result.existsInContacts)
    }

    @Test
    fun `lookupContact returns null when contact does not exist`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        
        coEvery { service.lookupContact(phoneNumber) } returns null

        // When
        val result = service.lookupContact(phoneNumber)

        // Then
        assertNull(result)
    }

    @Test
    fun `lookupContact handles malformed phone numbers`() = runTest {
        // Given
        val phoneNumber = "invalid-phone-number"
        
        coEvery { service.lookupContact(phoneNumber) } returns null

        // When
        val result = service.lookupContact(phoneNumber)

        // Then
        assertNull(result)
    }

    @Test
    fun `lookupContact returns contact with photo when available`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val photoUri = "content://contacts/photos/123"
        val contactInfo = TestDataFactory.createContactInfo(
            phoneNumber = phoneNumber,
            existsInContacts = true,
            photoUri = photoUri
        )
        
        coEvery { service.lookupContact(phoneNumber) } returns contactInfo

        // When
        val result = service.lookupContact(phoneNumber)

        // Then
        assertNotNull(result)
        assertEquals(photoUri, result.photoUri)
    }
}