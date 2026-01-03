package com.example.callguardian.lookup.sources

import com.example.callguardian.model.LookupResult
import com.squareup.okhttp3.mockwebserver.MockResponse
import com.squareup.okhttp3.mockwebserver.MockWebServer
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ApiSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiSource: AbstractApiSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        
        apiSource = object : AbstractApiSource(
            baseUrl = "http://localhost:${mockWebServer.port}/",
            apiKey = "test-api-key"
        ) {
            override suspend fun parseResponse(responseBody: String): LookupResult? {
                // Simple mock parser for testing
                return if (responseBody.contains("success")) {
                    LookupResult(
                        phoneNumber = "+15551234567",
                        displayName = "Test Contact",
                        carrier = "Test Carrier",
                        region = "Test Region",
                        lineType = "Mobile",
                        spamScore = 25,
                        tags = listOf("Test Tag"),
                        summary = "Test summary"
                    )
                } else {
                    null
                }
            }
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `successful API response returns parsed result`() = runBlocking {
        // Given
        val mockResponse = """
            {
                "success": true,
                "data": {
                    "name": "Test Contact",
                    "carrier": "Test Carrier"
                }
            }
        """.trimIndent()
        
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(mockResponse)
            .addHeader("Content-Type", "application/json"))

        // When
        val result = apiSource.lookup("+15551234567")

        // Then
        assertNotNull(result)
        assertEquals("+15551234567", result.phoneNumber)
        assertEquals("Test Contact", result.displayName)
        assertEquals("Test Carrier", result.carrier)
    }

    @Test
    fun `API returns 404 returns null`() = runBlocking {
        // Given
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(404)
            .setBody("Not Found"))

        // When
        val result = apiSource.lookup("+15551234567")

        // Then
        assertNull(result)
    }

    @Test
    fun `API returns 500 returns null`() = runBlocking {
        // Given
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(500)
            .setBody("Internal Server Error"))

        // When
        val result = apiSource.lookup("+15551234567")

        // Then
        assertNull(result)
    }

    @Test
    fun `API returns invalid JSON returns null`() = runBlocking {
        // Given
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("invalid json {"))

        // When
        val result = apiSource.lookup("+15551234567")

        // Then
        assertNull(result)
    }

    @Test
    fun `API returns empty response returns null`() = runBlocking {
        // Given
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(""))

        // When
        val result = apiSource.lookup("+15551234567")

        // Then
        assertNull(result)
    }

    @Test
    fun `API request includes correct headers`() = runBlocking {
        // Given
        val mockResponse = """
            {
                "success": true,
                "data": {}
            }
        """.trimIndent()
        
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(mockResponse)
            .addHeader("Content-Type", "application/json"))

        // When
        apiSource.lookup("+15551234567")

        // Then
        val request = mockWebServer.takeRequest()
        assertEquals("test-api-key", request.getHeader("X-API-Key"))
        assertEquals("application/json", request.getHeader("Accept"))
    }

    @Test
    fun `API request includes correct phone number in URL`() = runBlocking {
        // Given
        val mockResponse = """
            {
                "success": true,
                "data": {}
            }
        """.trimIndent()
        
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(mockResponse)
            .addHeader("Content-Type", "application/json"))

        // When
        apiSource.lookup("+15551234567")

        // Then
        val request = mockWebServer.takeRequest()
        assertTrue(request.path?.contains("15551234567") == true)
    }

    @Test
    fun `API handles network timeout`() = runBlocking {
        // Given
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("test")
            .setBodyDelay(5000)) // 5 second delay

        // When & Then
        // Should handle timeout gracefully and return null
        val result = apiSource.lookup("+15551234567")
        assertNull(result)
    }
}