package com.example.callguardian.lookup.sources

import com.example.callguardian.data.settings.LookupPreferences
import com.example.callguardian.lookup.ReverseLookupSource
import com.example.callguardian.model.LookupResult
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NumLookupApiSource @Inject constructor(
    private val preferences: LookupPreferences,
    private val client: OkHttpClient,
    moshi: Moshi
) : ReverseLookupSource {

    private val adapter = moshi.adapter(NumLookupResponse::class.java)

    override val id: String = "numlookup"
    override val displayName: String = "NumLookupAPI"

    override suspend fun lookup(phoneNumber: String): LookupResult? {
        val prefs = preferences.preferencesFlow.first()
        if (!prefs.hasNumLookupKey) return null

        val url = BASE_URL.toHttpUrl().newBuilder()
            .addPathSegment("v1")
            .addPathSegment("validate")
            .addPathSegment(phoneNumber)
            .addQueryParameter("apikey", prefs.numLookupApiKey)
            .build()

        val request = Request.Builder().url(url).get().build()

        val response = runCatching { client.newCall(request).execute() }
            .onFailure { Timber.w(it, "NumLookup request failed") }
            .getOrNull() ?: return null

        response.use { resp ->
            if (!resp.isSuccessful) return null
            val bodyString = resp.body?.string() ?: return null
            val payload = runCatching { adapter.fromJson(bodyString) }
                .onFailure { Timber.w(it, "Failed to parse NumLookup response") }
                .getOrNull() ?: return null

            if (payload.error != null) return null

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
