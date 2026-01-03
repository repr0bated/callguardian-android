package com.callguardian.app.lookup.sources

import com.callguardian.app.data.settings.LookupPreferences
import com.callguardian.app.lookup.ReverseLookupSource
import com.callguardian.app.model.LookupResult
import com.callguardian.app.util.exceptions.ApiException
import com.callguardian.app.util.exceptions.ExceptionFactory
import com.callguardian.app.util.exceptions.LookupException
import com.callguardian.app.util.logging.Logger
import com.callguardian.app.util.network.NetworkManager
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NumLookupApiSource @Inject constructor(
    private val preferences: LookupPreferences,
    private val client: OkHttpClient,
    private val networkManager: NetworkManager,
    moshi: Moshi
) : ReverseLookupSource {

    private val adapter = moshi.adapter(NumLookupResponse::class.java)

    override val id: String = "numlookup"
    override val displayName: String = "NumLookupAPI"

    override suspend fun lookup(phoneNumber: String): LookupResult? {
        val prefs = preferences.preferencesFlow.first()
        if (!prefs.hasNumLookupKey) {
            Logger.w("NumLookupAPI lookup skipped: no API key configured")
            return null
        }

        val url = BASE_URL.toHttpUrl().newBuilder()
            .addPathSegment("v1")
            .addPathSegment("validate")
            .addPathSegment(phoneNumber)
            .addQueryParameter("apikey", prefs.numLookupApiKey)
            .build()

        val request = Request.Builder().url(url).get().build()

        try {
            val response = networkManager.executeWithRetry(
                operationName = "numlookupapi_lookup",
                maxRetries = 3,
                block = { client.newCall(request).execute() }
            )

            response.use { resp ->
                if (!resp.isSuccessful) {
                    Logger.w("NumLookupAPI request failed with status: ${resp.code}")
                    return null
                }
                
                val bodyString = resp.body?.string() ?: run {
                    Logger.w("NumLookupAPI response body is null")
                    return null
                }
                
                val payload = runCatching { adapter.fromJson(bodyString) }
                    .onFailure {
                        Logger.e(it, "Failed to parse NumLookupAPI response: $bodyString")
                        throw ApiException("Failed to parse NumLookupAPI response", it)
                    }
                    .getOrNull() ?: return null

                if (payload.error != null) {
                    Logger.w("NumLookupAPI returned error: ${payload.error}")
                    return null
                }

                return LookupResult(
                    phoneNumber = phoneNumber,
                    displayName = payload.name,
                    carrier = payload.carrier,
                    region = payload.countryCode,
                    lineType = payload.lineType,
                    spamScore = when (payload.valid) {
                        true -> payload.spamScore ?: 0
                        else -> null
                    },
                    tags = listOfNotNull(payload.spamScore?.let { "Spam Score: $it" }),
                    source = displayName,
                    rawPayload = mapOf(
                        "valid" to payload.valid,
                        "carrier" to payload.carrier,
                        "country" to payload.countryCode,
                        "line_type" to payload.lineType
                    )
                )
            }
        } catch (e: ApiException) {
            Logger.e(e, "NumLookupAPI lookup failed for phone number: $phoneNumber")
            throw e
        } catch (e: Exception) {
            Logger.e(e, "Unexpected error during NumLookupAPI lookup for phone number: $phoneNumber")
            throw ExceptionFactory.createLookupException("NumLookupAPI lookup failed", e)
        }
    }

    private data class NumLookupResponse(
        val valid: Boolean?,
        val name: String?,
        val carrier: String?,
        @Json(name = "line_type") val lineType: String?,
        @Json(name = "country_code") val countryCode: String?,
        @Json(name = "spam_score") val spamScore: Int?,
        val error: String?
    )

    companion object {
        private const val BASE_URL = "https://api.numlookupapi.com/"
    }
}
