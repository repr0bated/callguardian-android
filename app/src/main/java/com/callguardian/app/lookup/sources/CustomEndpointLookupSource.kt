package com.callguardian.app.lookup.sources

import com.callguardian.app.data.settings.LookupPreferences
import com.callguardian.app.lookup.ReverseLookupSource
import com.callguardian.app.model.LookupResult
import com.callguardian.app.util.exceptions.ApiException
import com.callguardian.app.util.exceptions.ExceptionFactory
import com.callguardian.app.util.exceptions.LookupException
import com.callguardian.app.util.logging.Logger
import com.callguardian.app.util.network.NetworkManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomEndpointLookupSource @Inject constructor(
    private val preferences: LookupPreferences,
    private val client: OkHttpClient,
    private val networkManager: NetworkManager,
    moshi: Moshi
) : ReverseLookupSource {

    private val adapter = moshi.adapter<Map<String, Any?>>(
        Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    )

    override val id: String = "custom"
    override val displayName: String = "Custom Endpoint"

    override suspend fun lookup(phoneNumber: String): LookupResult? {
        val prefs = preferences.preferencesFlow.first()
        if (!prefs.hasCustomEndpoint) {
            Logger.w("Custom endpoint lookup skipped: no endpoint configured")
            return null
        }

        val encoded = URLEncoder.encode(phoneNumber, Charsets.UTF_8.name())
        val endpoint = prefs.customEndpoint.replace("{number}", encoded, ignoreCase = true)

        val requestBuilder = Request.Builder().url(endpoint)
        if (prefs.customHeaderName.isNotBlank() && prefs.customHeaderValue.isNotBlank()) {
            requestBuilder.header(prefs.customHeaderName, prefs.customHeaderValue)
        }

        try {
            val response = networkManager.executeWithRetry(
                operationName = "custom_endpoint_lookup",
                maxRetries = 3,
                block = { client.newCall(requestBuilder.build()).execute() }
            )

            response.use { resp ->
                if (!resp.isSuccessful) {
                    Logger.w("Custom endpoint request failed with status: ${resp.code}")
                    return null
                }
                
                val body = resp.body?.string()?.takeIf { it.isNotEmpty() } ?: run {
                    Logger.w("Custom endpoint response body is empty")
                    return null
                }
                
                val payload = runCatching { adapter.fromJson(body) }
                    .onFailure {
                        Logger.e(it, "Failed to parse custom endpoint response: $body")
                        throw ApiException("Failed to parse custom endpoint response", it)
                    }
                    .getOrNull() ?: emptyMap()

                val name = payload[KNOWN_KEYS.displayName] as? String
                    ?: payload["name"] as? String
                    ?: payload["caller_name"] as? String
                val carrier = payload[KNOWN_KEYS.carrier] as? String
                    ?: payload["company"] as? String
                val region = payload[KNOWN_KEYS.region] as? String
                    ?: payload["country"] as? String
                val spamScore = (payload[KNOWN_KEYS.spamScore] as? Number)?.toInt()

                return LookupResult(
                    phoneNumber = phoneNumber,
                    displayName = name,
                    carrier = carrier,
                    region = region,
                    spamScore = spamScore,
                    source = displayName,
                    rawPayload = payload
                )
            }
        } catch (e: ApiException) {
            Logger.e(e, "Custom endpoint lookup failed for phone number: $phoneNumber")
            throw e
        } catch (e: Exception) {
            Logger.e(e, "Unexpected error during custom endpoint lookup for phone number: $phoneNumber")
            throw ExceptionFactory.createLookupException("Custom endpoint lookup failed", e)
        }
    }

    private object KNOWN_KEYS {
        const val displayName = "display_name"
        const val carrier = "carrier"
        const val region = "region"
        const val spamScore = "spam_score"
    }
}
