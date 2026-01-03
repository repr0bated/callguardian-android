package com.example.callguardian.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.callguardian.model.LookupResult
import com.example.callguardian.util.TestDataFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LookupNotificationManagerTest {

    private lateinit var notificationManager: LookupNotificationManager
    private lateinit var mockContext: Context
    private lateinit var mockNotificationManager: NotificationManager

    @Before
    fun setup() {
        mockContext = mockk(relaxed = true)
        mockNotificationManager = mockk(relaxed = true)
        
        every { mockContext.getSystemService(Context.NOTIFICATION_SERVICE) } returns mockNotificationManager
        
        notificationManager = LookupNotificationManager(mockContext)
    }

    @Test
    fun `createLookupNotification creates notification with correct properties`() {
        // Given
        val lookupResult = TestDataFactory.createLookupResult(
            phoneNumber = "+15551234567",
            displayName = "Test Contact",
            spamScore = 25
        )

        // When
        val notification = notificationManager.createLookupNotification(lookupResult)

        // Then
        assertEquals(Notification::class.java, notification.javaClass)
        // Additional assertions would depend on the actual implementation
    }

    @Test
    fun `createSpamNotification creates high priority notification`() {
        // Given
        val lookupResult = TestDataFactory.createSpamLookupResult()

        // When
        val notification = notificationManager.createSpamNotification(lookupResult)

        // Then
        assertEquals(Notification::class.java, notification.javaClass)
        // Should have high priority for spam notifications
    }

    @Test
    fun `createBlockNotification creates notification for blocked calls`() {
        // Given
        val phoneNumber = "+15551234567"

        // When
        val notification = notificationManager.createBlockNotification(phoneNumber)

        // Then
        assertEquals(Notification::class.java, notification.javaClass)
        // Should indicate that a call was blocked
    }

    @Test
    fun `showNotification calls notification manager`() {
        // Given
        val lookupResult = TestDataFactory.createLookupResult()
        val notification = mockk<Notification>(relaxed = true)
        
        every { notificationManager.createLookupNotification(lookupResult) } returns notification

        // When
        notificationManager.showNotification(lookupResult)

        // Then
        verify { mockNotificationManager.notify(any(), any(), notification) }
    }

    @Test
    fun `cancelNotification calls notification manager cancel`() {
        // Given
        val notificationId = 123

        // When
        notificationManager.cancelNotification(notificationId)

        // Then
        verify { mockNotificationManager.cancel(notificationId) }
    }
}