# Devpost submission: Call Guardian

**Tagline:** A private scam shield that runs Gemma 4 on your phone, and can only act through typed, schema-checked methods that leave a receipt.

**Track:** Android (Firebase AI Logic hybrid inference)

## Inspiration
Scam texts about tolls, packages, and "is this still your number?" hit everyone, and the people most at risk are the least likely to paste a suspicious text into a chatbot. Your messages are also the most private data on your phone. I wanted protection that reads them *on the device*, works offline, and never lets an AI take an action it can't justify.

## What it does
* Screens every incoming **SMS** and **call**. Rules run in microseconds, then **Gemma 4 (Gemini Nano 4) on-device via Firebase AI Logic** reads the message privately. It works in airplane mode.
* When you want more, **Explain with Gemini** sends a *redacted* copy to cloud Gemini through the same Firebase AI Logic SDK, protected by App Check. You get the tactics being used and exactly what to do next.
* **Typed methods instead of free-form AI actions.** The model can only propose one method from a versioned registry: `guardian.call.block@v1`, `guardian.call.allow@v1`, `guardian.call.reject@v1`, `guardian.sms.quarantine@v1`, `guardian.report.scam@v1`, `guardian.contact.save@v1`, or `guardian.cloud.explain@v1`.
  * Each method has input and output JSON Schemas, a capability, and a device|cloud execution policy.
  * Proposals are validated against the schema and a capability policy before anything runs. The policy never blocks contacts, falls back from block to quarantine when confidence is low, and only offers cloud suggestions.
  * Every attempt writes a **receipt** to Room (input, schema version, where it ran, result), shown on the Receipts tab.
* **The schema is the single source of truth for actions and their UI.** Kotlin types are generated from the schemas at build time. The same schemas validate model output and render the confirmation sheets and receipt screens at runtime, so adding a method means adding a schema, not UI code.

## How we built it
* Kotlin, Jetpack Compose (Material 3), Hilt, Room, DataStore.
* **Firebase AI Logic:**
  * `firebase-ai` + `firebase-ai-ondevice` with `OnDeviceConfig(mode = ONLY_ON_DEVICE / PREFER_ON_DEVICE, modelOption = PREVIEW_FAST)`, which is Gemma 4 E2B / Nano 4 Fast via AICore. `generateObject()` uses KSP `@Generable` schemas, and `inferenceSource` is recorded on every verdict.
  * Cloud Gemini through an `ONLY_IN_CLOUD` model.
  * App Check with Play Integrity, plus the debug provider for development.
* JSON Schema draft-07 registry → `json-kotlin-schema-codegen` → generated `@Serializable` method types. The `networknt` validator checks model output, method inputs and outputs, and the registry itself.
* A schema-driven Compose renderer: enums become chips, `readOnly` becomes locked bound fields, `format: phone` gets a phone keyboard, `epoch-millis` displays as dates, with live validation.
* 38 JVM unit tests: renderer mapping, registry/enum consistency, binder output validated for every method, capability policy, verdict validation, rules and redaction.

## Challenges
* On-device inference is foreground-only and Telecom gives a call screener 5 seconds. Calls are decided by rules in time, and the on-device model reviews events as soon as the app is opened.
* Small models produce imperfect JSON. Invalid output is discarded and the rules decision stands; the model's opinion never silently wins.
* Keeping the Kotlin enum required by `@Generable`, the JSON Schemas and the registry in lockstep. A consistency test fails the build if they drift.

## What I learned
Hybrid inference works best as a *trust boundary*: private content stays on-device, explanations go to the cloud by consent, and typed methods plus receipts make the AI auditable.

## What's next
Signed registry updates, per-method permissions UI, Korean prompts (on-device is supported), and a Play Store release so Play Integrity can enforce App Check for everyone.

## Built during the submission period
The pre-existing repo was a lookup-only screening app that didn't build. All of the hybrid AI, the typed-method registry, receipts, the schema renderer, the new UI and the fixes landed on the `buildhybridai` branch in November 2026. See README → *What was built during the submission period*.

**Links:** https://github.com/repr0bated/callguardian-android/tree/buildhybridai · Apache-2.0 · by Jeremy Hobson
