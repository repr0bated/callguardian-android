package com.example.callguardian.model

import kotlin.math.roundToInt

data class AiAssessment(
    val label: String,
    val confidence: Double
) {
    val confidencePercent: Int = ((confidence * 100).coerceIn(0.0, 100.0)).roundToInt()
}
