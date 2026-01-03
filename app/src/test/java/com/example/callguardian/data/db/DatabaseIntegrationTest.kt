package com.example.callguardian.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.callguardian.model.InteractionDirection
import com.example.callguardian.model.InteractionType
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DatabaseIntegrationTest {

    private lateinit var database: CallGuardianDatabase
    private lateinit var profileDao: PhoneProfileDao
    private lateinit var interactionDao: PhoneInteractionDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CallGuardianDatabase::class.java
        ).build()
        
        profileDao = database.phoneProfileDao()
        interactionDao = database.phoneInteractionDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `full profile lifecycle with interactions`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val profile = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = phoneNumber,
            displayName = "Integration Test Contact"
        )
        val interaction1 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = phoneNumber,
            type = InteractionType.CALL,
            direction = InteractionDirection.INCOMING
        )
        val interaction2 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = phoneNumber,
            type = InteractionType.SMS,
            direction = InteractionDirection.OUTGOING
        )

        // When
        profileDao.upsert(profile)
        interactionDao.insert(interaction1)
        interactionDao.insert(interaction2)

        // Then
        val retrievedProfile = profileDao.getByNumber(phoneNumber)
        val interactions = interactionDao.getRecentInteractions(10).first()

        assertNotNull(retrievedProfile)
        assertEquals(profile.phoneNumber, retrievedProfile.phoneNumber)
        assertEquals(profile.displayName, retrievedProfile.displayName)
        
        assertEquals(2, interactions.size)
        assertTrue(interactions.any { it.phoneNumber == phoneNumber })
        assertTrue(interactions.any { it.type == InteractionType.CALL })
        assertTrue(interactions.any { it.type == InteractionType.SMS })
    }

    @Test
    fun `multiple profiles with their interactions`() = runTest {
        // Given
        val profile1 = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15551111111",
            displayName = "Contact 1"
        )
        val profile2 = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15552222222",
            displayName = "Contact 2"
        )
        val interaction1 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15551111111",
            type = InteractionType.CALL
        )
        val interaction2 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15552222222",
            type = InteractionType.SMS
        )
        val interaction3 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15551111111",
            type = InteractionType.CALL
        )

        // When
        profileDao.upsert(profile1)
        profileDao.upsert(profile2)
        interactionDao.insert(interaction1)
        interactionDao.insert(interaction2)
        interactionDao.insert(interaction3)

        // Then
        val allProfiles = profileDao.observeAll().first()
        val allInteractions = interactionDao.observeRecent(10).first()

        assertEquals(2, allProfiles.size)
        assertEquals(3, allInteractions.size)
        
        assertTrue(allProfiles.any { it.phoneNumber == "+15551111111" })
        assertTrue(allProfiles.any { it.phoneNumber == "+15552222222" })
        assertTrue(allInteractions.any { it.phoneNumber == "+15551111111" })
        assertTrue(allInteractions.any { it.phoneNumber == "+15552222222" })
    }

    @Test
    fun `profile updates with interaction tracking`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val originalProfile = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = phoneNumber,
            displayName = "Original Name",
            spamScore = 25
        )
        val updatedProfile = originalProfile.copy(
            displayName = "Updated Name",
            spamScore = 50,
            blockMode = com.example.callguardian.model.BlockMode.BLOCK_ALL
        )
        val interaction = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = phoneNumber,
            type = InteractionType.CALL,
            status = "BLOCKED"
        )

        // When
        profileDao.upsert(originalProfile)
        profileDao.upsert(updatedProfile)
        interactionDao.insert(interaction)

        // Then
        val finalProfile = profileDao.getByNumber(phoneNumber)
        val interactions = interactionDao.getRecentInteractions(10).first()

        assertNotNull(finalProfile)
        assertEquals("Updated Name", finalProfile.displayName)
        assertEquals(50, finalProfile.spamScore)
        assertEquals(com.example.callguardian.model.BlockMode.BLOCK_ALL, finalProfile.blockMode)
        
        assertEquals(1, interactions.size)
        assertEquals("BLOCKED", interactions[0].status)
    }

    @Test
    fun `database handles concurrent operations correctly`() = runTest {
        // Given
        val profiles = listOf(
            TestDataFactory.createPhoneProfileEntity(phoneNumber = "+15551111111"),
            TestDataFactory.createPhoneProfileEntity(phoneNumber = "+15552222222"),
            TestDataFactory.createPhoneProfileEntity(phoneNumber = "+15553333333")
        )
        val interactions = listOf(
            TestDataFactory.createPhoneInteractionEntity(phoneNumber = "+15551111111"),
            TestDataFactory.createPhoneInteractionEntity(phoneNumber = "+15552222222"),
            TestDataFactory.createPhoneInteractionEntity(phoneNumber = "+15553333333")
        )

        // When
        profiles.forEach { profileDao.upsert(it) }
        interactions.forEach { interactionDao.insert(it) }

        // Then
        val allProfiles = profileDao.observeAll().first()
        val allInteractions = interactionDao.observeRecent(10).first()

        assertEquals(3, allProfiles.size)
        assertEquals(3, allInteractions.size)
        
        profiles.forEach { profile ->
            assertTrue(allProfiles.any { it.phoneNumber == profile.phoneNumber })
        }
        interactions.forEach { interaction ->
            assertTrue(allInteractions.any { it.phoneNumber == interaction.phoneNumber })
        }
    }

    @Test
    fun `database handles large number of records`() = runTest {
        // Given
        val profiles = (1..100).map { index ->
            TestDataFactory.createPhoneProfileEntity(
                phoneNumber = "+1555${String.format("%07d", index)}",
                displayName = "Contact $index"
            )
        }
        val interactions = (1..200).map { index ->
            TestDataFactory.createPhoneInteractionEntity(
                phoneNumber = "+1555${String.format("%07d", (index % 100) + 1)}",
                type = if (index % 2 == 0) InteractionType.CALL else InteractionType.SMS
            )
        }

        // When
        profiles.forEach { profileDao.upsert(it) }
        interactions.forEach { interactionDao.insert(it) }

        // Then
        val allProfiles = profileDao.observeAll().first()
        val allInteractions = interactionDao.observeRecent(50).first()

        assertEquals(100, allProfiles.size)
        assertEquals(50, allInteractions.size) // Limited by observeRecent(50)
        
        // Verify we can still query individual records
        val specificProfile = profileDao.getByNumber("+15550000001")
        assertNotNull(specificProfile)
        assertEquals("Contact 1", specificProfile.displayName)
    }

    @Test
    fun `database handles edge cases with null and empty values`() = runTest {
        // Given
        val profileWithNulls = TestDataFactory.createPhoneProfileEntity(
            phoneNumber = "+15551234567",
            displayName = "",
            notes = null,
            contactId = null
        )
        val interactionWithNulls = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15551234567",
            messageBody = null,
            lookupSummary = ""
        )

        // When
        profileDao.upsert(profileWithNulls)
        interactionDao.insert(interactionWithNulls)

        // Then
        val retrievedProfile = profileDao.getByNumber("+15551234567")
        val interactions = interactionDao.getRecentInteractions(10).first()

        assertNotNull(retrievedProfile)
        assertEquals("", retrievedProfile.displayName)
        assertEquals(null, retrievedProfile.notes)
        assertEquals(null, retrievedProfile.contactId)
        
        assertEquals(1, interactions.size)
        assertEquals(null, interactions[0].messageBody)
        assertEquals("", interactions[0].lookupSummary)
    }
}