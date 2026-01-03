package com.example.callguardian.ai

import com.example.callguardian.data.settings.LookupPreferences
import com.example.callguardian.model.AiAssessment
import com.example.callguardian.model.LookupResult
import com.example.callguardian.util.exceptions.ApiException
import com.example.callguardian.util.exceptions.ExceptionFactory
import com.example.callguardian.util.exceptions.LookupException
import com.example.callguardian.util.logging.Logger
import com.example.callguardian.util.network.NetworkManager
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRiskScorer @Inject constructor(
    private val preferences: LookupPreferences,
    private val client: OkHttpClient,
    private val networkManager: NetworkManager,
    moshi: Moshi
) {

    private val requestAdapter = moshi.adapter(RequestPayload::class.java)
    private val responseAdapter = moshi.adapter<List<ResponseItem>>(
        Types.newParameterizedType(List::class.java, ResponseItem::class.java)
    )
    private val listResponseAdapter = moshi.adapter<List<List<ResponseItem>>>(
        Types.newParameterizedType(
            List::class.java,
            Types.newParameterizedType(List::class.java, ResponseItem::class.java)
        )
    )

    suspend fun evaluate(
        phoneNumber: String,
        messageBody: String?,
        lookupResult: LookupResult?
    ): AiAssessment? {
        val prefs = preferences.preferencesFlow.first()
        if (!prefs.hasHuggingFaceCredentials) {
            Logger.w("AI risk evaluation skipped: no Hugging Face credentials configured")
            return null
        }

        val input = buildInput(phoneNumber, messageBody, lookupResult)
        if (input.isBlank()) {
            Logger.w("AI risk evaluation skipped: no input data")
            return null
        }

        val url = "https://api-inference.huggingface.co/models/${prefs.huggingFaceModelId}"
        val body = requestAdapter.toJson(RequestPayload(inputs = input))
            .toRequestBody(JSON_MEDIA_TYPE)

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer ${prefs.huggingFaceApiKey}")
            .header("Accept", "application/json")
            .post(body)
            .build()

        try {
            val response = networkManager.executeWithRetry("AI Risk Evaluation") { client.newCall(request).execute() }

            response.use { resp ->
                if (!resp.isSuccessful) {
                    Logger.w("AI risk response unsuccessful: ${resp.code}")
                    return null
                }
                val rawBody = resp.body?.string() ?: run {
                    Logger.w("AI risk response body is null")
                    return null
                }

                val best = parseResponse(rawBody) ?: return null
                val label = best.label?.takeIf { it.isNotBlank() } ?: return null
                val score = best.score ?: return null

                return AiAssessment(
                    label = label.trim(),
                    confidencePercent = (score.coerceIn(0.0, 1.0) * 100).toInt()
                )
            }
        } catch (e: ApiException) {
            Logger.e(e, "AI risk evaluation failed for phone number: $phoneNumber")
            throw e
        } catch (e: Exception) {
            Logger.e(e, "Unexpected error during AI risk evaluation for phone number: $phoneNumber")
            throw ExceptionFactory.createLookupException("AI risk evaluation failed", e)
        }
    }

    private fun parseResponse(rawBody: String): ResponseItem? {
        responseAdapter.fromJson(rawBody)?.let { list ->
            return list.maxByOrNull { it.score ?: 0.0 }
        }

        listResponseAdapter.fromJson(rawBody)?.let { outer ->
            return outer.flatten().maxByOrNull { it.score ?: 0.0 }
        }

        Logger.w("AI risk response parse failed: $rawBody")
        return null
    }

    private fun buildInput(
        phoneNumber: String,
        messageBody: String?,
        lookupResult: LookupResult?
    ): String {
        val normalized = phoneNumber.filter { it.isDigit() || it == '+' }
        val builder = StringBuilder()
        builder.appendLine("Phone: $normalized")
        lookupResult?.displayName?.takeIf { it.isNotBlank() }?.let {
            builder.appendLine("Name: $it")
        }
        lookupResult?.tags?.takeIf { it.isNotEmpty() }?.let {
            builder.appendLine("Tags: ${it.joinToString()}")
        }
        lookupResult?.carrier?.takeIf { it.isNotBlank() }?.let {
            builder.appendLine("Carrier: $it")
        }
        lookupResult?.region?.takeIf { it.isNotBlank() }?.let {
            builder.appendLine("Region: $it")
        }
        messageBody?.takeIf { it.isNotBlank() }?.let {
            builder.appendLine("Message: $it")
        }
        builder.appendLine("Task: Classify whether this interaction is spam or safe.")
        return builder.toString()
    }

    private data class RequestPayload(val inputs: String)

    private data class ResponseItem(
        val label: String?,
        val score: Double?,
        @Json(name = "error") val error: String? = null
    )

    companion object {
        private val JSON_MEDIA_TYPE = "application/json".toMediaType()
    }
}
