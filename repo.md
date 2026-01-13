# Call Guardian - Repository Overview

## Project Description
**Call Guardian** is an Android application designed for advanced call and message screening. It performs real-time reverse lookups against multiple data sources and uses AI-driven risk scoring to help users identify and manage spam interactions.

## Core Tech Stack
- **Language**: Kotlin 1.9
- **UI Framework**: Jetpack Compose (Material 3)
- **Dependency Injection**: Hilt (Dagger)
- **Persistence**: Room Database (SQLite) & Jetpack DataStore (Preferences)
- **Networking**: Retrofit 2, OkHttp 4, Moshi
- **Asynchrony**: Kotlin Coroutines & Flow
- **Background Processing**: WorkManager
- **Logging**: Timber
- **External APIs & AI**: 
    - NumLookup API
    - Abstract API
    - Hugging Face Inference API
    - Google Firebase Vertex AI (Gemini)

## Project Structure
- `app/src/main/java/com/example/callguardian/`:
    - `ai/`: Risk scoring implementation using Hugging Face and Vertex AI.
    - `data/`: Persistence layer including Room DB (`db/`), Repository pattern (`repository/`), and DataStore (`settings/`).
    - `di/`: Hilt modules for dependency injection.
    - `lookup/`: Reverse lookup management, provider implementations (`sources/`), performance optimization, and health monitoring.
    - `model/`: Shared data models and domain entities.
    - `notifications/`: UI-level interaction handling via notifications.
    - `service/`: Core background services including `CallScreeningService`, `ContactSyncService`, and `ContactLookupService`.
    - `sms/`: Broadcast receivers for SMS interception.
    - `ui/`: Compose screens, components, and application theme.

## Configuration & CI/CD
- **Build System**: Gradle Kotlin DSL (`.gradle.kts`)
- **CI/CD Configuration**: 
    - **GitHub Actions**: Automated Build, Unit Testing, Linting, and Play Store Internal Release (via `android-release.yml`).
    - **Bitrise**: Configured for mobile-specific workflows (`bitrise.yml`).
    - **Codemagic**: Multi-platform CI/CD support (`codemagic.yaml`).
- **Android Specs**: 
    - Target SDK: 34 (Android 14)
    - Min SDK: 29 (Android 10)
    - Java/JVM: 17

## Key Functionality
1. **Intelligent Call Screening**: Leverages `CallScreeningService` to intercept calls with millisecond-latency lookups.
2. **SMS Spam Detection**: Acts as a default SMS handler to analyze and filter incoming messages.
3. **Multi-Source Lookup Pipeline**: Sequential lookup strategy across multiple APIs with automatic fallback and caching.
4. **AI Risk Assessment**: Real-time spam probability estimation using BERT-based models (Hugging Face) or Large Language Models (Vertex AI).
5. **Contact Synchronization**: Automated background sync to distinguish known contacts from unknown potential spam.
6. **System Health Monitoring**: Proactive monitoring of API availability and service performance.
7. **Blocklist & History**: Comprehensive local management of interaction history and granular blocking rules.
