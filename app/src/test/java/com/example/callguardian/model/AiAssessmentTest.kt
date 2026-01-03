package com.example.callguardian.model

import com.example.callguardian.util.TestDataFactory
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AiAssessmentTest {

    @Test
    fun `AiAssessment constructor sets all properties correctly`() {
        // Given
        val label = "High Risk"
        val confidencePercent = 95
        val explanation = "High probability of spam based on message content"

        // When
        val assessment = AiAssessment(
            label = label,
            confidencePercent = confidencePercent,
            explanation = explanation
        )

        // Then
        assertEquals(label, assessment.label)
        assertEquals(confidencePercent, assessment.confidencePercent)
        assertEquals(explanation, assessment.explanation)
    }

    @Test
    fun `AiAssessment with default values`() {
        // When
        val assessment = AiAssessment(
            label = "Low Risk",
            confidencePercent = 50
        )

        // Then
        assertEquals("Low Risk", assessment.label)
        assertEquals(50, assessment.confidencePercent)
        assertEquals("", assessment.explanation)
    }

    @Test
    fun `AiAssessment equality works correctly`() {
        // Given
        val assessment1 = TestDataFactory.createAiAssessment()
        val assessment2 = TestDataFactory.createAiAssessment()

        // Then
        assertEquals(assessment1, assessment2)
        assertEquals(assessment1.hashCode(), assessment2.hashCode())
    }

    @Test
    fun `AiAssessment toString includes key fields`() {
        // Given
        val assessment = TestDataFactory.createAiAssessment(
            label = "High Risk",
            confidencePercent = 95,
            explanation = "Test explanation"
        )

        // When
        val toString = assessment.toString()

        // Then
        assertTrue(toString.contains("label=High Risk"))
        assertTrue(toString.contains("confidencePercent=95"))
        assertTrue(toString.contains("explanation=Test explanation"))
    }

    @Test
    fun `AiAssessment confidence percent validation`() {
        // Given
        val validConfidence = 75
        val assessment = AiAssessment(
            label = "Medium Risk",
            confidencePercent = validConfidence
        )

        // Then
        assertEquals(validConfidence, assessment.confidencePercent)
    }
}