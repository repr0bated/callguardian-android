package com.callguardian.app.lookup.sources

import com.callguardian.app.data.settings.LookupPreferences
import com.callguardian.app.lookup.ReverseLookupSource
import com.callguardian.app.model.LookupResult
import com.callguardian.app.util.exceptions.ApiException
import com.callguardian.app.util.exceptions.ExceptionFactory
import com.callguardian.app.util.exceptions.LookupException
import com.callguardian.app.util.handling.ErrorHandling
import com.callguardian.app.util.logging.Logger
import com.callguardian.app.util.network.NetworkManager
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
    private val networkManager: NetworkManager,
    moshi: Moshi
) : ReverseLookupSource {

    override val id: String = "abstractapi"
    override val displayName: String = "AbstractAPI"

    private val adapter = moshi.adapter(AbstractApiResponse::class.java)

    override suspend fun lookup(phoneNumber: String): LookupResult? {
        val prefs = preferences.preferencesFlow.first()
        if (!prefs.hasAbstractKey) {
            Logger.w("AbstractAPI lookup skipped: no API key configured")
            return null
        }

        val url = BASE_URL.toHttpUrl().newBuilder()
            .addQueryParameter("api_key", prefs.abstractApiKey)
            .addQueryParameter("phone", phoneNumber)
            .build()

        val request = Request.Builder().url(url).build()
        
        try {
            val response = networkManager.executeWithRetry(
                operationName = "abstractapi_lookup",
                maxRetries = 3,
                block = { client.newCall(request).execute() }
            )
            
            response.use { resp ->
                if (!resp.isSuccessful) {
                    Logger.w("AbstractAPI request failed with status: ${resp.code}")
                    return null
                }
                
                val bodyString = resp.body?.string() ?: run {
                    Logger.w("AbstractAPI response body is null")
                    return null
                }
                
                val payload = runCatching { adapter.fromJson(bodyString) }
                    .onFailure {
                        Logger.e(it, "Failed to parse AbstractAPI response: $bodyString")
                        throw ApiException("Failed to parse AbstractAPI response", it)
                    }
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
        } catch (e: ApiException) {
            Logger.e(e, "AbstractAPI lookup failed for phone number: $phoneNumber")
            throw e
        } catch (e: Exception) {
            Logger.e(e, "Unexpected error during AbstractAPI lookup for phone number: $phoneNumber")
            throw ExceptionFactory.createLookupException("AbstractAPI lookup failed", e)
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
