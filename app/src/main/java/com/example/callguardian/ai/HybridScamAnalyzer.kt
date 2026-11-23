package com.example.callguardian.ai

import com.example.callguardian.BuildConfig
import com.example.callguardian.data.settings.OnDeviceModelChoice
import com.example.callguardian.model.VerdictSource
import com.google.firebase.Firebase
import com.google.firebase.ai.DownloadStatus
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.InferenceMode
import com.google.firebase.ai.InferenceSource
import com.google.firebase.ai.OnDeviceConfig
import com.google.firebase.ai.OnDeviceModelOption
import com.google.firebase.ai.OnDeviceModelStatus
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/** Raw output of one model call, before schema validation. */
data class ModelRun(
    val rawText: String?,
    val source: VerdictSource,
    val modelName: String?,
    val latencyMs: Long,
)

enum class DeviceModelState { UNKNOWN, UNAVAILABLE, DOWNLOADABLE, DOWNLOADING, AVAILABLE }

/**
 * Every generative call in the app goes through Firebase AI Logic (`com.google.firebase:firebase-ai`
 * + `firebase-ai-ondevice`). Two model instances:
 *
 * 1. **Triage** — `OnDeviceConfig(mode = ONLY_ON_DEVICE | PREFER_ON_DEVICE, modelOption = PREVIEW_FAST)`.
 *    `PREVIEW_FAST` asks AICore for Gemini Nano 4 Fast (Gemma 4 E2B); `PREVIEW` for Nano 4 Full
 *    (Gemma 4 E4B). In private mode it is `ONLY_ON_DEVICE`, so message text never leaves the phone.
 * 2. **Deep analysis** — `ONLY_IN_CLOUD` Gemini, used only when the user taps "Deep analysis",
 *    and only with redacted input.
 *
 * Both use `generateObject()` with KSP-generated schemas (`@Generable`), so the model is
 * constrained to JSON; the app then re-validates the raw text against the published JSON Schema.
 */
@Singleton
class HybridScamAnalyzer @Inject constructor() {

    private val cloudModelName = BuildConfig.CLOUD_MODEL

    private fun option(choice: OnDeviceModelChoice): OnDeviceModelOption = when (choice) {
        OnDeviceModelChoice.PREVIEW_FAST -> OnDeviceModelOption.PREVIEW_FAST
        OnDeviceModelChoice.PREVIEW -> OnDeviceModelOption.PREVIEW
        OnDeviceModelChoice.STABLE -> OnDeviceModelOption.STABLE
    }

    private val triageModels = HashMap<Pair<OnDeviceModelChoice, Boolean>, GenerativeModel>()

    @Synchronized
    fun triageModel(choice: OnDeviceModelChoice, privateMode: Boolean): GenerativeModel =
        triageModels.getOrPut(choice to privateMode) {
            Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
                modelName = cloudModelName,
                generationConfig = generationConfig { temperature = 0.1f; maxOutputTokens = 400 },
                onDeviceConfig = OnDeviceConfig(
                    mode = if (privateMode) InferenceMode.ONLY_ON_DEVICE else InferenceMode.PREFER_ON_DEVICE,
                    maxOutputTokens = 256,
                    temperature = 0.1f,
                    modelOption = option(choice),
                ),
            )
        }

    private val deepModel: GenerativeModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = cloudModelName,
            generationConfig = generationConfig { temperature = 0.2f; maxOutputTokens = 900 },
            onDeviceConfig = OnDeviceConfig(mode = InferenceMode.ONLY_IN_CLOUD),
        )
    }

    // ---- on-device model lifecycle (AICore) ----

    suspend fun deviceState(choice: OnDeviceModelChoice): DeviceModelState = try {
        when (triageModel(choice, true).onDeviceExtension?.checkStatus()) {
            OnDeviceModelStatus.AVAILABLE -> DeviceModelState.AVAILABLE
            OnDeviceModelStatus.DOWNLOADABLE -> DeviceModelState.DOWNLOADABLE
            OnDeviceModelStatus.DOWNLOADING -> DeviceModelState.DOWNLOADING
            OnDeviceModelStatus.UNAVAILABLE -> DeviceModelState.UNAVAILABLE
            else -> DeviceModelState.UNKNOWN
        }
    } catch (e: Throwable) {
        Timber.w(e, "On-device status check failed")
        DeviceModelState.UNAVAILABLE
    }

    fun download(choice: OnDeviceModelChoice): Flow<DownloadStatus> =
        triageModel(choice, true).onDeviceExtension?.download() ?: emptyFlow()

    suspend fun warmUp(choice: OnDeviceModelChoice) {
        runCatching { triageModel(choice, true).onDeviceExtension?.warmUp() }
            .onFailure { Timber.w(it, "warmUp failed") }
    }

    /** e.g. "nano-v4-fast" — shown in the UI so it's clear which Gemma 4 / Nano variant ran. */
    suspend fun deviceModelName(choice: OnDeviceModelChoice): String? =
        runCatching { triageModel(choice, true).onDeviceExtension?.getModelName() }.getOrNull()

    // ---- inference ----

    suspend fun triage(prompt: String, choice: OnDeviceModelChoice, privateMode: Boolean): ModelRun {
        val model = triageModel(choice, privateMode)
        val t0 = System.currentTimeMillis()
        val result = model.generateObject(ScamVerdict.firebaseAISchema(), prompt)
        val latency = System.currentTimeMillis() - t0
        val source = if (result.response.inferenceSource == InferenceSource.ON_DEVICE) VerdictSource.ON_DEVICE else VerdictSource.IN_CLOUD
        val name = if (source == VerdictSource.ON_DEVICE) deviceModelName(choice) ?: choice.title else cloudModelName
        return ModelRun(result.response.text, source, name, latency)
    }

    suspend fun deepAnalysis(prompt: String): ModelRun {
        val t0 = System.currentTimeMillis()
        val result = deepModel.generateObject(DeepAnalysis.firebaseAISchema(), prompt)
        return ModelRun(result.response.text, VerdictSource.IN_CLOUD, cloudModelName, System.currentTimeMillis() - t0)
    }
}
