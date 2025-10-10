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
class AbstractApiSource @Inject constructor(
    private val preferences: LookupPreferences,
    private val client: OkHttpClient,
    moshi: Moshi
) : ReverseLookupSource {

    override val id: String = "abstractapi"
    override val displayName: String = "AbstractAPI"

    private val adapter = moshi.adapter(AbstractApiResponse::class.java)

    override suspend fun lookup(phoneNumber: String): LookupResult? {
        val prefs = preferences.preferencesFlow.first()
        if (!prefs.hasAbstractKey) return null

        val url = BASE_URL.toHttpUrl().newBuilder()
            .addQueryParameter("api_key", prefs.abstractApiKey)
            .addQueryParameter("phone", phoneNumber)
            .build()

        val request = Request.Builder().url(url).build()
        val response = runCatching { client.newCall(request).execute() }
            .onFailure { Timber.w(it, "AbstractAPI request failed") }
            .getOrNull() ?: return null

        response.use { resp ->
            if (!resp.isSuccessful) return null
            val bodyString = resp.body?.string() ?: return null
            val payload = runCatching { adapter.fromJson(bodyString) }
                .onFailure { Timber.w(it, "Failed to parse AbstractAPI response") }
                .getOrNull() ?: return null

            return LookupResult(
                phoneNumber = phoneNumber,
                displayName = payload.owner?.name,
                carrier = payload.carrier,
                region = payload.location?.let { "${it.city ?: ""} ${it.country ?: ""}".trim() },
                lineType = payload.type,
                spamScore = payload.risk?.score,
                tags = payload.risk?.level?.let { listOf(it) } ?: emptyList(),
                source = displayName,
                rawPayload = mapOf(
                    "valid" to payload.valid,
                    "carrier" to payload.carrier,
                    "risk" to payload.risk,
                    "location" to payload.location
                )
            )
        }
    }

    private data class AbstractApiResponse(
        val valid: Boolean?,
        val carrier: String?,
        val type: String?,
        val owner: Owner?,
        val location: Location?,
        val risk: Risk?
    ) {
        data class Owner(@Json(name = "name") val name: String?)
        data class Location(val city: String?, val country: String?)
        data class Risk(val level: String?, val score: Int?)
    }

    companion object {
        private const val BASE_URL = "https://phonevalidation.abstractapi.com/v1/"
    }
}
