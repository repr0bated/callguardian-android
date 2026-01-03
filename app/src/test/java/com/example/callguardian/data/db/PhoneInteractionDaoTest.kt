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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PhoneInteractionDaoTest {

    private lateinit var database: CallGuardianDatabase
    private lateinit var dao: PhoneInteractionDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CallGuardianDatabase::class.java
        ).build()
        
        dao = database.phoneInteractionDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insert and retrieve interaction successfully`() = runTest {
        // Given
        val interaction = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15551234567",
            type = InteractionType.CALL,
            direction = InteractionDirection.INCOMING,
            status = "ALLOWED"
        )

        // When
        dao.insert(interaction)
        val result = dao.getRecentInteractions(1).first()

        // Then
        assertEquals(1, result.size)
        val retrieved = result[0]
        assertEquals(interaction.phoneNumber, retrieved.phoneNumber)
        assertEquals(interaction.type, retrieved.type)
        assertEquals(interaction.direction, retrieved.direction)
        assertEquals(interaction.status, retrieved.status)
        assertEquals(interaction.messageBody, retrieved.messageBody)
        assertEquals(interaction.lookupSummary, retrieved.lookupSummary)
    }

    @Test
    fun `observeRecent returns interactions in correct order`() = runTest {
        // Given
        val interaction1 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15551111111",
            type = InteractionType.CALL,
            direction = InteractionDirection.INCOMING
        )
        val interaction2 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15552222222",
            type = InteractionType.SMS,
            direction = InteractionDirection.OUTGOING
        )
        val interaction3 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15553333333",
            type = InteractionType.CALL,
            direction = InteractionDirection.MISSED
        )

        // When
        dao.insert(interaction1)
        dao.insert(interaction2)
        dao.insert(interaction3)
        val result = dao.observeRecent(3).first()

        // Then
        assertEquals(3, result.size)
        // Should be ordered by timestamp descending (most recent first)
        assertEquals(interaction3.phoneNumber, result[0].phoneNumber)
        assertEquals(interaction2.phoneNumber, result[1].phoneNumber)
        assertEquals(interaction1.phoneNumber, result[2].phoneNumber)
    }

    @Test
    fun `getRecentInteractions limits results correctly`() = runTest {
        // Given
        repeat(10) { index ->
            val interaction = TestDataFactory.createPhoneInteractionEntity(
                phoneNumber = "+1555${String.format("%07d", index)}",
                type = InteractionType.CALL,
                direction = InteractionDirection.INCOMING
            )
            dao.insert(interaction)
        }

        // When
        val result = dao.getRecentInteractions(5).first()

        // Then
        assertEquals(5, result.size)
    }

    @Test
    fun `observeRecent returns empty list when no interactions exist`() = runTest {
        // When
        val result = dao.observeRecent(10).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `interactions with message body are correctly stored`() = runTest {
        // Given
        val messageBody = "This is a test SMS message"
        val interaction = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15551234567",
            type = InteractionType.SMS,
            direction = InteractionDirection.INCOMING,
            messageBody = messageBody
        )

        // When
        dao.insert(interaction)
        val result = dao.getRecentInteractions(1).first()

        // Then
        assertEquals(1, result.size)
        assertEquals(messageBody, result[0].messageBody)
    }

    @Test
    fun `interactions with null message body are correctly stored`() = runTest {
        // Given
        val interaction = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15551234567",
            type = InteractionType.CALL,
            direction = InteractionDirection.INCOMING,
            messageBody = null
        )

        // When
        dao.insert(interaction)
        val result = dao.getRecentInteractions(1).first()

        // Then
        assertEquals(1, result.size)
        assertEquals(null, result[0].messageBody)
    }

    @Test
    fun `interactions with lookup summary are correctly stored`() = runTest {
        // Given
        val lookupSummary = "Spam Risk: High • Carrier: Test Carrier"
        val interaction = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15551234567",
            type = InteractionType.CALL,
            direction = InteractionDirection.INCOMING,
            lookupSummary = lookupSummary
        )

        // When
        dao.insert(interaction)
        val result = dao.getRecentInteractions(1).first()

        // Then
        assertEquals(1, result.size)
        assertEquals(lookupSummary, result[0].lookupSummary)
    }

    @Test
    fun `multiple interactions for same phone number are stored correctly`() = runTest {
        // Given
        val phoneNumber = "+15551234567"
        val interaction1 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = phoneNumber,
            type = InteractionType.CALL,
            direction = InteractionDirection.INCOMING
        )
        val interaction2 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = phoneNumber,
            type = InteractionType.SMS,
            direction = InteractionDirection.INCOMING
        )
        val interaction3 = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = phoneNumber,
            type = InteractionType.CALL,
            direction = InteractionDirection.OUTGOING
        )

        // When
        dao.insert(interaction1)
        dao.insert(interaction2)
        dao.insert(interaction3)
        val result = dao.getRecentInteractions(10).first()

        // Then
        assertEquals(3, result.size)
        result.forEach { interaction ->
            assertEquals(phoneNumber, interaction.phoneNumber)
        }
    }

    @Test
    fun `interactions with different statuses are correctly stored`() = runTest {
        // Given
        val allowedInteraction = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15551111111",
            status = "ALLOWED"
        )
        val blockedInteraction = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15552222222",
            status = "BLOCKED"
        )
        val missedInteraction = TestDataFactory.createPhoneInteractionEntity(
            phoneNumber = "+15553333333",
            status = "MISSED"
        )

        // When
        dao.insert(allowedInteraction)
        dao.insert(blockedInteraction)
        dao.insert(missedInteraction)
        val result = dao.getRecentInteractions(10).first()

        // Then
        assertEquals(3, result.size)
        assertTrue(result.any { it.status == "ALLOWED" })
        assertTrue(result.any { it.status == "BLOCKED" })
        assertTrue(result.any { it.status == "MISSED" })
    }
}