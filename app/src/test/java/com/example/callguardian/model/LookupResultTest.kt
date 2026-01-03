package com.example.callguardian.model

import com.example.callguardian.util.TestDataFactory
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LookupResultTest {

    @Test
    fun `LookupResult constructor sets all properties correctly`() {
        // Given
        val phoneNumber = "+15551234567"
        val displayName = "Test Contact"
        val carrier = "Test Carrier"
        val region = "Test Region"
        val lineType = "Mobile"
        val spamScore = 25
        val tags = listOf("Test Tag", "Another Tag")
        val summary = "Test summary"

        // When
        val result = LookupResult(
            phoneNumber = phoneNumber,
            displayName = displayName,
            carrier = carrier,
            region = region,
            lineType = lineType,
            spamScore = spamScore,
            tags = tags,
            summary = summary
        )

        // Then
        assertEquals(phoneNumber, result.phoneNumber)
        assertEquals(displayName, result.displayName)
        assertEquals(carrier, result.carrier)
        assertEquals(region, result.region)
        assertEquals(lineType, result.lineType)
        assertEquals(spamScore, result.spamScore)
        assertEquals(tags, result.tags)
        assertEquals(summary, result.summary)
    }

    @Test
    fun `LookupResult with default values`() {
        // When
        val result = LookupResult(
            phoneNumber = "+15551234567"
        )

        // Then
        assertEquals("+15551234567", result.phoneNumber)
        assertEquals("", result.displayName)
        assertEquals("", result.carrier)
        assertEquals("", result.region)
        assertEquals("", result.lineType)
        assertEquals(0, result.spamScore)
        assertTrue(result.tags.isEmpty())
        assertEquals("", result.summary)
    }

    @Test
    fun `LookupResult equality works correctly`() {
        // Given
        val phoneNumber = "+15551234567"
        val result1 = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)
        val result2 = TestDataFactory.createLookupResult(phoneNumber = phoneNumber)

        // Then
        assertEquals(result1, result2)
        assertEquals(result1.hashCode(), result2.hashCode())
    }

    @Test
    fun `LookupResult toString includes key fields`() {
        // Given
        val result = TestDataFactory.createLookupResult(
            phoneNumber = "+15551234567",
            displayName = "Test Contact",
            spamScore = 25
        )

        // When
        val toString = result.toString()

        // Then
        assertTrue(toString.contains("phoneNumber=+15551234567"))
        assertTrue(toString.contains("displayName=Test Contact"))
        assertTrue(toString.contains("spamScore=25"))
    }
}