# Call Guardian

Call Guardian is an Android call- and message-screening app that performs reverse lookups against configurable free data sources, lets you reject or end calls, block future interactions, or save trusted numbers. The project is implemented in Kotlin with Jetpack Compose, Hilt DI, and Room persistence.

## Key Features
- **Call interception** via `CallScreeningService` with quick actions (reject, block, allow, save).
- **SMS interception** for the default SMS role with reverse lookup notifications and blocklist enforcement.
- **Reverse lookup pipeline** with built-in connectors for [NumLookup API](https://numlookupapi.com/) and [Abstract API](https://www.abstractapi.com/phone-validation-api), plus a configurable custom JSON endpoint.
- **Optional AI risk scoring** via Hugging Face Inference API for spam probability estimates on call/SMS metadata.
- **Blocklist management UI** to toggle call/message blocking modes per number.
- **Local data store** (Room) for lookup cache and interaction history.
- **Notification actions** for one-tap block/allow/reject/save flows.

## Project Layout

```
CallGuardian/
├── app/
│   ├── build.gradle.kts        # Module configuration (Compose, Hilt, Room, etc.)
│   ├── src/main/
│   │   ├── AndroidManifest.xml # Service/receiver declarations and permissions
│   │   ├── java/com/example/callguardian/
│   │   │   ├── CallGuardianApplication.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/…          # Room entities/DAO/repository + DataStore prefs
│   │   │   ├── lookup/…        # Reverse lookup manager and sources
│   │   │   ├── notifications/… # Notification manager + action receiver
│   │   │   ├── service/…       # CallScreeningService implementation
│   │   │   └── sms/…           # SMS BroadcastReceiver
│   │   └── res/…               # Compose theme, strings, icons
├── build.gradle.kts            # Root Gradle version catalog
├── gradle.properties
└── settings.gradle.kts
```

## Prerequisites

- Android Studio Giraffe (2022.3.1) or newer with Android Gradle Plugin 8.2+
- Android SDK Platform 34, Build-Tools 34.x, and Google Play Services repository
- JDK 17 (matching the Gradle/AGP configuration)
- Local Gradle wrapper (`gradlew`). Generate it once via `gradle wrapper --gradle-version 8.5` (or create from Android Studio) if it isn’t already present.

## First-Time Setup

1. Open the project in Android Studio and let it sync/create the Gradle wrapper.
2. Create a keystore (for debug and release) if you don’t already have one. Keep it outside of VCS and configure via **Build > Generate Signed Bundle/APK…**.
3. Acquire optional reverse lookup credentials:
   - [NumLookup API](https://numlookupapi.com/) free tier API key.
- [Abstract Phone Validation](https://www.abstractapi.com/phone-validation-api) API key.
- Any additional JSON endpoint that returns caller details (configure in Settings in-app).
- (Optional) A free [Hugging Face](https://huggingface.co/) access token if you want AI-powered risk scoring.
4. Install the app on a physical device running Android 10 (API 29) or newer. Emulators do not support real call/SMS interception.

## Granting The Required Roles & Permissions

After installing the app:

1. Launch Call Guardian.
2. Accept the `POST_NOTIFICATIONS` permission prompt (Android 13+).
3. Tap **Activate** to grant the **Call Screening** role. You must be the default call screening app before the OS delivers incoming call events.
4. Tap **Set Default** to make Call Guardian the **default SMS** app (required to receive SMS broadcasts). You can revert later via Settings.
5. From system settings, also grant:
   - `READ_CALL_LOG` and `READ_PHONE_STATE` permissions.
   - `RECEIVE_SMS`, `READ_SMS`, and `RECEIVE_MMS` permissions (should be automatic once the app is default SMS, but verify under App Info).
   - `ANSWER_PHONE_CALLS` to let the reject action end active calls.
   - `READ/WRITE_CONTACTS` if you want the one-tap “Save Contact” action.

## Configuring Reverse Lookup Sources

- Open the app, scroll to **Lookup Services**, and paste the API keys you obtained. Keys are stored in Jetpack DataStore and injected into lookup sources.
- For the custom endpoint:
  - Provide a URL containing `{number}` as a placeholder (e.g., `https://your-free-endpoint.example/lookup?phone={number}`).
  - Optional header name/value support lets you set API tokens (e.g., `Authorization: Bearer …`).
  - Responses should be JSON objects; the app attempts to map common fields (`display_name`, `carrier`, `region`, `spam_score`). Everything else is stored in the raw payload for future extensions.
- Lookups execute sequentially; the first source returning data wins. Extend `lookup/sources` with additional connectors if needed (bind them in `LookupModule`).
- For AI augmentation, add your Hugging Face API key and model ID (e.g., `mrm8488/bert-tiny-finetuned-sms-spam-detection`) in-app. The app sends a lightweight text prompt with the number metadata/message to `https://api-inference.huggingface.co/models/{modelId}` and annotates results with the model’s label and confidence.

## Building Debug & Release APKs

1. Ensure the Gradle wrapper exists (`./gradlew`). If not, create it as noted in **Prerequisites**.
2. For a debug build:
   ```bash
   ./gradlew assembleDebug
   ```
   Install the resulting APK from `app/build/outputs/apk/debug/`.
3. For a release build suitable for Play Store upload:
   ```bash
   ./gradlew assembleRelease
   ```
   Sign the APK/AAB with your upload keystore via Android Studio or the `signingConfig` (keystore files/credentials must stay outside source control).
4. Run instrumentation/unit tests as needed:
   ```bash
   ./gradlew testDebugUnitTest connectedDebugAndroidTest
   ```

## Preparing For Play Store Submission

- Update `app/build.gradle.kts` with proper `applicationId`, `versionCode`, and `versionName` before release builds.
- Provide a privacy policy explaining what data is collected (call/SMS metadata) and how it is stored locally. Mention that reverse lookup requests call third-party services and comply with their terms of use.
- Supply runtime rationale dialogs if you want more context when requesting sensitive permissions (CALL_LOG, SMS, CONTACTS).
- Verify target API level >= 33 (currently 34) and that all permission declarations fall under permitted use-cases. Attach a short video capture demonstrating call screening behavior if Google requests it during review.
- Use Play Console’s **App content** section to declare sensitive permissions and roles (Call Screening, Default SMS, Read/Send SMS).

## Extending The Project

- Implement additional reverse lookup providers by creating new `ReverseLookupSource` implementations and binding them with `@IntoSet` in `LookupModule`.
- Enhance call handling (e.g., auto voicemail, message replies) via `TelecomManager` APIs.
- Sync blocklists with server storage or share data across devices with DataStore/Cloud sync.
- Integrate WorkManager to refresh stale lookup data periodically.

## Notes & Limitations

- Call/SMS interception requires the app to be the default handler. Only one app can hold those roles at a time.
- Reverse lookup accuracy depends entirely on the upstream APIs. Free tiers may rate-limit or restrict commercial use; review their ToS.
- The notification “Reject Call” action depends on `ANSWER_PHONE_CALLS` permission and may fail on some OEM builds.
- Emulator builds cannot receive real telephony broadcasts; use a physical device for validation.

## License

No explicit license is provided. Add one if you intend to distribute the source.
