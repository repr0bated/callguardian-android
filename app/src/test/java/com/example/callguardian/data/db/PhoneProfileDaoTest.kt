package com.example.callguardian.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.callguardian.util.TestDataFactory
import com.example.callguardian.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PhoneProfileDaoTest {

    private lateinit var database: CallGuardianDatabase
    private lateinit var dao: PhoneProfileDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CallGuardianDatabase::class.java
        ).build()
        
        dao = database.phoneProfileDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insert and retrieve profile successfully`() = runTest {
        // Given
        val profile = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15551234567",
            displayName = "Test Contact"
        )

        // When
        dao.upsert(profile)
        val result = dao.getByNumber("+15551234567")

        // Then
        assertNotNull(result)
        assertEquals(profile.phoneNumber, result.phoneNumber)
        assertEquals(profile.displayName, result.displayName)
        assertEquals(profile.carrier, result.carrier)
        assertEquals(profile.region, result.region)
        assertEquals(profile.lineType, result.lineType)
        assertEquals(profile.spamScore, result.spamScore)
        assertEquals(profile.tags, result.tags)
        assertEquals(profile.blockMode, result.blockMode)
        assertEquals(profile.notes, result.notes)
        assertEquals(profile.contactId, result.contactId)
        assertEquals(profile.isExistingContact, result.isExistingContact)
    }

    @Test
    fun `update existing profile successfully`() = runTest {
        // Given
        val originalProfile = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15551234567",
            displayName = "Original Name",
            spamScore = 25
        )
        val updatedProfile = originalProfile.copy(
            displayName = "Updated Name",
            spamScore = 50,
            blockMode = com.example.callguardian.model.BlockMode.BLOCK_ALL
        )

        // When
        dao.upsert(originalProfile)
        dao.upsert(updatedProfile)
        val result = dao.getByNumber("+15551234567")

        // Then
        assertNotNull(result)
        assertEquals(updatedProfile.displayName, result.displayName)
        assertEquals(updatedProfile.spamScore, result.spamScore)
        assertEquals(updatedProfile.blockMode, result.blockMode)
    }

    @Test
    fun `updateBlockMode updates only block mode`() = runTest {
        // Given
        val profile = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15551234567",
            displayName = "Test Contact",
            blockMode = com.example.callguardian.model.BlockMode.NONE
        )
        
        dao.upsert(profile)

        // When
        dao.updateBlockMode("+15551234567", com.example.callguardian.model.BlockMode.BLOCK_ALL.name)
        val result = dao.getByNumber("+15551234567")

        // Then
        assertNotNull(result)
        assertEquals(com.example.callguardian.model.BlockMode.BLOCK_ALL, result.blockMode)
        assertEquals("Test Contact", result.displayName) // Other fields unchanged
        assertEquals(25, result.spamScore) // Other fields unchanged
    }

    @Test
    fun `updateContactInfo updates contact information`() = runTest {
        // Given
        val profile = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15551234567",
            displayName = "Test Contact",
            contactId = null,
            isExistingContact = false
        )
        
        dao.upsert(profile)

        // When
        val newContactId = 12345L
        dao.updateContactInfo("+15551234567", newContactId, true)
        val result = dao.getByNumber("+15551234567")

        // Then
        assertNotNull(result)
        assertEquals(newContactId, result.contactId)
        assertTrue(result.isExistingContact)
        assertEquals("Test Contact", result.displayName) // Other fields unchanged
    }

    @Test
    fun `getByNumber returns null for non-existent profile`() = runTest {
        // When
        val result = dao.getByNumber("+15559999999")

        // Then
        assertNull(result)
    }

    @Test
    fun `observeAll returns all profiles`() = runTest {
        // Given
        val profile1 = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15551111111",
            displayName = "Contact 1"
        )
        val profile2 = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15552222222",
            displayName = "Contact 2"
        )

        // When
        dao.upsert(profile1)
        dao.upsert(profile2)
        val result = dao.observeAll().first()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.any { it.phoneNumber == "+15551111111" })
        assertTrue(result.any { it.phoneNumber == "+15552222222" })
    }

    @Test
    fun `observeAll returns empty list when no profiles exist`() = runTest {
        // When
        val result = dao.observeAll().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `upsert handles null values correctly`() = runTest {
        // Given
        val profile = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15551234567",
            displayName = "Test Contact",
            notes = null,
            contactId = null
        )

        // When
        dao.upsert(profile)
        val result = dao.getByNumber("+15551234567")

        // Then
        assertNotNull(result)
        assertEquals("Test Contact", result.displayName)
        assertNull(result.notes)
        assertNull(result.contactId)
    }

    @Test
    fun `profile with high spam score is correctly stored`() = runTest {
        // Given
        val profile = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15551234567",
            spamScore = 95,
            tags = listOf("Spam", "High Risk", "Telemarketer")
        )

        // When
        dao.upsert(profile)
        val result = dao.getByNumber("+15551234567")

        // Then
        assertNotNull(result)
        assertEquals(95, result.spamScore)
        assertEquals(listOf("Spam", "High Risk", "Telemarketer"), result.tags)
    }

    @Test
    fun `profile with empty tags is correctly stored`() = runTest {
        // Given
        val profile = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15551234567",
            tags = emptyList()
        )

        // When
        dao.upsert(profile)
        val result = dao.getByNumber("+15551234567")

        // Then
        assertNotNull(result)
        assertTrue(result.tags.isEmpty())
    }
}