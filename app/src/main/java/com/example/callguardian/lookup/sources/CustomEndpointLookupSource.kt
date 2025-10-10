package com.example.callguardian.lookup.sources

import com.example.callguardian.data.settings.LookupPreferences
import com.example.callguardian.lookup.ReverseLookupSource
import com.example.callguardian.model.LookupResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomEndpointLookupSource @Inject constructor(
    private val preferences: LookupPreferences,
    private val client: OkHttpClient,
    moshi: Moshi
) : ReverseLookupSource {

    private val adapter = moshi.adapter<Map<String, Any?>>(
        Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    )

    override val id: String = "custom"
    override val displayName: String = "Custom Endpoint"

    override suspend fun lookup(phoneNumber: String): LookupResult? {
        val prefs = preferences.preferencesFlow.first()
        if (!prefs.hasCustomEndpoint) return null

        val encoded = URLEncoder.encode(phoneNumber, Charsets.UTF_8.name())
        val endpoint = prefs.customEndpoint.replace("{number}", encoded, ignoreCase = true)

        val requestBuilder = Request.Builder().url(endpoint)
        if (prefs.customHeaderName.isNotBlank() && prefs.customHeaderValue.isNotBlank()) {
            requestBuilder.header(prefs.customHeaderName, prefs.customHeaderValue)
        }

        val response = runCatching { client.newCall(requestBuilder.build()).execute() }
            .onFailure { Timber.w(it, "Custom lookup request failed") }
            .getOrNull() ?: return null

        response.use { resp ->
            if (!resp.isSuccessful) return null
            val body = resp.body?.string()?.takeIf { it.isNotEmpty() } ?: return null
            val payload = runCatching { adapter.fromJson(body) }
                .onFailure { Timber.w(it, "Failed to parse custom lookup response") }
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
    }

    private object KNOWN_KEYS {
        const val displayName = "display_name"
        const val carrier = "carrier"
        const val region = "region"
        const val spamScore = "spam_score"
    }
}
