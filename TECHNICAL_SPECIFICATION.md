# CallGuardian Android - Technical Specification

## Document Information
- **Version:** 1.0
- **Application ID:** `com.example.callguardian`
- **Minimum SDK:** 29 (Android 10)
- **Target SDK:** 34 (Android 14)
- **Kotlin Version:** 1.9.22
- **Compose Compiler:** 1.5.8

---

## Table of Contents
1. [Executive Summary](#1-executive-summary)
2. [Architecture Overview](#2-architecture-overview)
3. [Component Specifications](#3-component-specifications)
4. [Dependencies](#4-dependencies)
5. [Configuration Requirements](#5-configuration-requirements)
6. [Code Review Findings](#6-code-review-findings)
7. [Security Considerations](#7-security-considerations)
8. [Recommendations](#8-recommendations)

---

## 1. Executive Summary

CallGuardian is a modern Android application designed to screen incoming calls and SMS messages, providing users with reverse phone number lookup capabilities, AI-based spam risk assessment, and comprehensive contact management features. The application follows clean architecture principles with MVVM pattern, leverages Jetpack Compose for UI, and uses Hilt for dependency injection.

### Key Features
- Real-time call screening via Android Telecom API
- SMS interception and spam detection
- Multi-source reverse phone number lookup with fallback
- AI-powered risk scoring via HuggingFace integration
- Contact synchronization with local address book
- Encrypted storage for sensitive API credentials
- Circuit breaker pattern for service reliability

---

## 2. Architecture Overview

### 2.1 Architectural Pattern
```
┌─────────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                         │
│  ┌──────────────────┐  ┌──────────────────┐  ┌───────────────┐  │
│  │   MainActivity   │  │   MainViewModel  │  │  Compose UI   │  │
│  └────────┬─────────┘  └────────┬─────────┘  └───────────────┘  │
└───────────│────────────────────│────────────────────────────────┘
            │                    │
            ▼                    ▼
┌─────────────────────────────────────────────────────────────────┐
│                        DOMAIN LAYER                              │
│  ┌──────────────────┐  ┌──────────────────┐  ┌───────────────┐  │
│  │ LookupRepository │  │  ContactService  │  │   AiScorer    │  │
│  └────────┬─────────┘  └────────┬─────────┘  └───────┬───────┘  │
└───────────│────────────────────│─────────────────────│──────────┘
            │                    │                     │
            ▼                    ▼                     ▼
┌─────────────────────────────────────────────────────────────────┐
│                         DATA LAYER                               │
│  ┌──────────┐  ┌──────────────┐  ┌─────────────┐  ┌───────────┐ │
│  │   Room   │  │ DataStore    │  │  OkHttp/    │  │   Cache   │ │
│  │ Database │  │ Preferences  │  │  Retrofit   │  │  Manager  │ │
│  └──────────┘  └──────────────┘  └─────────────┘  └───────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 Key Design Patterns
| Pattern | Implementation | Location |
|---------|---------------|----------|
| MVVM | ViewModel + StateFlow | `ui/MainViewModel.kt` |
| Repository | Data abstraction | `data/repository/LookupRepository.kt` |
| Dependency Injection | Hilt/Dagger | `di/*Module.kt` |
| Circuit Breaker | Service resilience | `lookup/ServiceHealthMonitor.kt` |
| Factory | Exception creation | `util/exceptions/ExceptionFactory.kt` |
| Observer | Flow-based reactivity | All DAO/Repository classes |

---

## 3. Component Specifications

### 3.1 Call Screening Service

**File:** `service/CallGuardianCallScreeningService.kt`

#### Purpose
Intercepts incoming calls via Android's `CallScreeningService` API, performs lookup operations, and provides caller identification.

#### Features
- Android Telecom API integration
- Background coroutine processing with `SupervisorJob`
- Notification dispatch for incoming calls
- Graceful call response handling

#### Configuration
```xml
<service
    android:name=".service.CallGuardianCallScreeningService"
    android:exported="true"
    android:foregroundServiceType="dataSync"
    android:permission="android.permission.BIND_SCREENING_SERVICE">
    <intent-filter>
        <action android:name="android.telecom.CallScreeningService" />
    </intent-filter>
</service>
```

#### Dependencies
- `LookupNotificationManager` (injected)
- Coroutines (`Dispatchers.IO`, `SupervisorJob`)

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| Error Handling | **NEEDS IMPROVEMENT** | Missing try-catch in `onScreenCall` coroutine |
| Timeout | DEFINED | `LOOKUP_TIMEOUT_MS = 2500L` (unused currently) |
| Lookup Integration | **INCOMPLETE** | Calls `notifyBlockedCall` but doesn't perform actual lookup |
| Job Cancellation | GOOD | Properly cancels job in `onDestroy` |

---

### 3.2 SMS Receiver

**File:** `sms/SmsReceiver.kt`

#### Purpose
Intercepts incoming SMS messages via `BroadcastReceiver`, performs spam analysis, and optionally blocks suspicious messages.

#### Features
- High-priority broadcast receiver (priority: 999)
- Full SMS/MMS support including WAP Push
- Message body aggregation from multi-part SMS
- Async processing with `goAsync()` for extended execution

#### Configuration
```xml
<receiver
    android:name=".sms.SmsReceiver"
    android:exported="true"
    android:permission="android.permission.BROADCAST_SMS">
    <intent-filter android:priority="999">
        <action android:name="android.provider.Telephony.SMS_DELIVER" />
        <action android:name="android.provider.Telephony.WAP_PUSH_DELIVER" />
        <action android:name="android.provider.Telephony.SMS_RECEIVED" />
    </intent-filter>
</receiver>
```

#### Dependencies
- `LookupRepository` (injected)
- `LookupNotificationManager` (injected)

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| Async Handling | GOOD | Uses `goAsync()` with `pendingResult.finish()` |
| Error Handling | **NEEDS IMPROVEMENT** | No try-catch around coroutine operations |
| API Compatibility | GOOD | Build.VERSION check for KITKAT+ |
| Broadcast Abort | CORRECT | Properly calls `abortBroadcast()` when blocking |

---

### 3.3 Lookup Repository

**File:** `data/repository/LookupRepository.kt`

#### Purpose
Central data coordination layer that orchestrates phone number lookups, AI assessments, contact merging, and database persistence.

#### Features
- Multi-source lookup orchestration
- AI risk assessment integration
- Contact information merging
- Profile persistence with upsert operations
- Interaction logging
- Graceful degradation on database failures

#### API Surface
```kotlin
suspend fun performLookup(phoneNumber: String, type: InteractionType,
                          direction: InteractionDirection, messageBody: String?): LookupOutcome
suspend fun updateBlockMode(phoneNumber: String, blockMode: BlockMode)
suspend fun updateContactInfo(phoneNumber: String, contactId: Long?, isExistingContact: Boolean)
suspend fun getProfile(phoneNumber: String): PhoneProfileEntity?
fun observeProfiles(): Flow<List<PhoneProfileEntity>>
fun observeRecentInteractions(limit: Int = 50): Flow<List<PhoneInteractionEntity>>
suspend fun isExistingContact(phoneNumber: String): Boolean
```

#### Dependencies
- `ReverseLookupManager`
- `PhoneProfileDao`, `PhoneInteractionDao`
- `AiRiskScorer`
- `ContactLookupService`
- `DatabaseManager`
- `CoroutineDispatcher` (IO)

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| Error Handling | EXCELLENT | Comprehensive exception catching with graceful degradation |
| Thread Safety | GOOD | Uses `withContext(ioDispatcher)` consistently |
| Data Merging | GOOD | Contact priority over lookup for display names |
| Logging | GOOD | Uses structured `Logger` with context |

---

### 3.4 Reverse Lookup Manager

**File:** `lookup/ReverseLookupManager.kt`

#### Purpose
Orchestrates multiple reverse phone lookup sources with parallel execution, caching, and fallback strategies.

#### Features
- Parallel lookup execution (first successful wins)
- Sequential fallback on parallel failure
- In-memory caching with TTL
- Health-aware source ordering
- Performance tracking per source

#### Lookup Strategy
```
1. Check cache → return if found
2. Execute parallel lookup across healthy sources
3. If parallel fails, execute sequential lookup with source ordering
4. Cache successful result
5. Return result or null
```

#### Dependencies
- `Set<ReverseLookupSource>` (multi-binding)
- `LookupCacheManager`
- `ServiceHealthMonitor`
- `CoroutineDispatcher` (IO)

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| Parallelism | GOOD | Uses `async` with proper coroutine scope |
| Cache Strategy | GOOD | Cache miss graceful handling |
| Error Resilience | EXCELLENT | `runCatching` with failure logging |
| Performance | **POTENTIAL ISSUE** | `deferredResults.forEach { await() }` waits sequentially; should use `select` for true first-result |

---

### 3.5 Service Health Monitor

**File:** `lookup/ServiceHealthMonitor.kt`

#### Purpose
Implements circuit breaker pattern for API source reliability monitoring with automatic health checks.

#### Features
- Circuit breaker states: CLOSED, OPEN, HALF_OPEN
- Response time tracking
- Success rate calculation (>80% threshold)
- Periodic health checks (5-minute interval)
- Best source selection by performance

#### Circuit Breaker Logic
```
CLOSED → OPEN: 5 consecutive failures
OPEN → HALF_OPEN: 1 success attempt
HALF_OPEN → CLOSED: 1 success
HALF_OPEN → OPEN: 3 failures
```

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| Thread Safety | GOOD | Uses `Mutex` and `ConcurrentHashMap` |
| Health Check | **INCOMPLETE** | Periodic check doesn't actually call source |
| Metrics Reset | **MISSING** | No mechanism to reset/decay old metrics |
| Memory | GOOD | Bounded by source count |

---

### 3.6 Lookup Cache Manager

**File:** `data/cache/LookupCacheManager.kt`

#### Purpose
Provides TTL-based in-memory caching for lookup results with LRU eviction.

#### Features
- 7-day default TTL
- 10,000 maximum entries
- LRU-like eviction (lowest hit count)
- Hit count tracking
- Cache statistics

#### Configuration
| Parameter | Value |
|-----------|-------|
| `DEFAULT_TTL_SECONDS` | 604,800 (7 days) |
| `MAX_CACHE_SIZE` | 10,000 entries |
| Eviction Rate | 25% when full |

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| Thread Safety | EXCELLENT | Uses `Mutex` for all operations |
| Memory Estimation | **APPROXIMATE** | Fixed 1KB per entry assumption |
| Eviction Strategy | GOOD | Hit-count based (not true LRU) |
| Persistence | **MISSING** | Cache lost on process death |

---

### 3.7 AI Risk Scorer

**File:** `ai/AiRiskScorer.kt`

#### Purpose
Integrates with HuggingFace Inference API for ML-based spam/risk classification.

#### Features
- HuggingFace model integration
- Configurable model ID
- Network retry with exponential backoff
- Input normalization and context building
- Flexible response parsing (array or nested array)

#### API Integration
```
POST https://api-inference.huggingface.co/models/{modelId}
Authorization: Bearer {apiKey}
Content-Type: application/json

{"inputs": "Phone: +1234567890\nName: ...\nTags: ...\nTask: Classify..."}
```

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| Credential Security | GOOD | Uses encrypted preferences |
| Error Handling | GOOD | Graceful null returns |
| Input Validation | **BASIC** | Only checks for blank input |
| Rate Limiting | **NOT HANDLED** | No client-side rate limiting |

---

### 3.8 Notification Manager

**File:** `notifications/LookupNotificationManager.kt`

#### Purpose
Creates and manages rich notifications for lookup results with quick actions.

#### Features
- High-importance notification channel
- Quick actions: Block, Allow, Save Contact, Reject Call
- BigTextStyle for AI assessment display
- Contact indicator display

#### Actions
| Action | Intent Extra | Purpose |
|--------|--------------|---------|
| `ACTION_BLOCK` | `EXTRA_PHONE_NUMBER` | Block caller/sender |
| `ACTION_ALLOW` | `EXTRA_PHONE_NUMBER` | Whitelist number |
| `ACTION_SAVE` | `EXTRA_PHONE_NUMBER` | Add to contacts |
| `ACTION_REJECT` | `EXTRA_PHONE_NUMBER` | Reject current call |

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| API Compatibility | EXCELLENT | Proper version checks for channels |
| PendingIntent Flags | GOOD | Uses `FLAG_IMMUTABLE` on M+ |
| Channel Creation | GOOD | Idempotent `createNotificationChannel` |
| Notification ID | **COLLISION RISK** | `hashCode()` may collide |

---

### 3.9 Database Layer

**File:** `data/db/CallGuardianDatabase.kt`

#### Purpose
Room database definition with two tables for phone profiles and interaction history.

#### Schema

**Table: `phone_profiles`**
| Column | Type | Constraints |
|--------|------|-------------|
| `phone_number` | TEXT | PRIMARY KEY |
| `display_name` | TEXT | |
| `carrier` | TEXT | |
| `region` | TEXT | |
| `line_type` | TEXT | |
| `last_lookup_at` | INTEGER | Instant converter |
| `spam_score` | INTEGER | |
| `tags` | TEXT | JSON array converter |
| `block_mode` | TEXT | Enum converter |
| `notes` | TEXT | |
| `contact_id` | INTEGER | Nullable |
| `is_existing_contact` | INTEGER | Boolean |

**Table: `phone_interactions`**
| Column | Type | Constraints |
|--------|------|-------------|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT |
| `phone_number` | TEXT | |
| `type` | TEXT | CALL, SMS |
| `direction` | TEXT | INCOMING, OUTGOING, MISSED |
| `timestamp` | INTEGER | |
| `status` | TEXT | BLOCKED, ALLOWED |
| `message_body` | TEXT | Nullable |
| `lookup_summary` | TEXT | |

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| Schema Export | ENABLED | Good for migrations |
| Type Converters | COMPLETE | All custom types covered |
| Indexes | **MISSING** | No indexes on frequently queried columns |
| Migration Strategy | **UNDEFINED** | No migrations defined for v1→v2 |

---

### 3.10 Preferences & Encryption

**Files:** `data/settings/LookupPreferences.kt`, `data/settings/EncryptedPreferences.kt`

#### Purpose
Manages user preferences with encrypted storage for sensitive API credentials.

#### Features
- Android Security Crypto integration
- Encrypted DataStore for API keys
- Plain text DataStore for non-sensitive settings
- Fallback to plain text on encryption failure
- Credential state observation via Flow

#### Encrypted Fields
- NumLookup API Key
- Abstract API Key
- Custom Endpoint Configuration
- HuggingFace Credentials

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| Encryption | GOOD | Uses AndroidX Security Crypto |
| Fallback | **SECURITY CONCERN** | Falls back to plain text on failure |
| Migration | **MISSING** | No migration from plain to encrypted |
| Key Rotation | **NOT IMPLEMENTED** | No mechanism for key rotation |

---

### 3.11 Exception Handling Framework

**File:** `util/exceptions/CallGuardianException.kt`

#### Purpose
Provides comprehensive, typed exception hierarchy with user-friendly messages.

#### Exception Types
| Exception | Error Code | Use Case |
|-----------|------------|----------|
| `NetworkException` | `NETWORK_ERROR` | HTTP failures, timeouts |
| `DatabaseException` | `DATABASE_ERROR` | Room operations |
| `ApiException` | `API_ERROR` | External API failures |
| `ValidationException` | `VALIDATION_ERROR` | Input validation |
| `PermissionException` | `PERMISSION_ERROR` | Missing permissions |
| `StorageException` | `STORAGE_ERROR` | File operations |
| `AiProcessingException` | `AI_PROCESSING_ERROR` | ML model failures |
| `ContactException` | `CONTACT_ERROR` | Contacts provider |
| `LookupException` | `LOOKUP_ERROR` | Phone lookup failures |
| `CacheException` | `CACHE_ERROR` | Cache operations |
| `ServiceException` | `SERVICE_ERROR` | Background service |
| `ConfigurationException` | `CONFIGURATION_ERROR` | App configuration |
| `SecurityException` | `SECURITY_ERROR` | Crypto operations |

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| Coverage | EXCELLENT | Comprehensive exception types |
| User Messages | EXCELLENT | Human-readable, actionable |
| Factory Pattern | GOOD | Consistent exception creation |
| Serialization | **MISSING** | Not Parcelable for IPC |

---

### 3.12 Main ViewModel

**File:** `ui/MainViewModel.kt`

#### Purpose
Primary ViewModel managing UI state, user preferences, and contact synchronization.

#### State Classes
```kotlin
sealed class ContactSyncUiState {
    object Idle : ContactSyncUiState()
    object Analyzing : ContactSyncUiState()
    object NoChanges : ContactSyncUiState()
    data class SyncAvailable(val syncResult: ContactSyncResult.ChangesDetected)
    object Applying : ContactSyncUiState()
    object Success : ContactSyncUiState()
    data class Error(val message: String) : ContactSyncUiState()
}

data class MainUiState(
    val profiles: List<PhoneProfileEntity>,
    val recentInteractions: List<PhoneInteractionEntity>,
    val preferences: LookupPreferencesState,
    val contactSyncState: ContactSyncUiState
)
```

#### Review Notes
| Aspect | Status | Notes |
|--------|--------|-------|
| State Management | EXCELLENT | Reactive StateFlow pattern |
| Error Handling | GOOD | State-based error display |
| Lifecycle | GOOD | Uses `viewModelScope` |
| Repository Calls | **MISSING** | `analyzeContactSync` not implemented in repository |

---

## 4. Dependencies

### 4.1 Core Dependencies
| Dependency | Version | Purpose |
|------------|---------|---------|
| Kotlin | 1.9.22 | Language |
| Compose BOM | 2024.02.00 | UI Framework |
| Material3 | BOM-managed | Design System |
| Navigation Compose | 2.7.6 | Navigation |
| Hilt | 2.50 | Dependency Injection |
| Room | 2.6.1 | Database |
| DataStore | 1.0.0 | Preferences |
| Security Crypto | 1.1.0-alpha06 | Encryption |

### 4.2 Network Dependencies
| Dependency | Version | Purpose |
|------------|---------|---------|
| Retrofit | 2.9.0 | REST Client |
| OkHttp | 4.12.0 | HTTP Client |
| Moshi | 1.15.0 | JSON Parsing |

### 4.3 Utility Dependencies
| Dependency | Version | Purpose |
|------------|---------|---------|
| Coroutines | 1.7.3 | Async Processing |
| Timber | 5.0.1 | Logging |
| WorkManager | 2.9.0 | Background Jobs |

### 4.4 Test Dependencies
| Dependency | Version | Purpose |
|------------|---------|---------|
| JUnit | 4.13.2 | Unit Testing |
| MockK | 1.13.8 | Mocking |
| Robolectric | 4.10.3 | Android Unit Tests |
| Truth | 1.4.2 | Assertions |
| Espresso | 3.5.1 | UI Testing |
| MockWebServer | 4.12.0 | API Testing |

### 4.5 Dependency Graph
```
app
├── androidx.compose (UI)
│   ├── material3
│   ├── ui-tooling
│   └── navigation-compose
├── com.google.dagger:hilt-android (DI)
├── androidx.room (Database)
├── androidx.datastore (Preferences)
├── androidx.security:security-crypto (Encryption)
├── com.squareup.retrofit2 (Network)
│   └── converter-moshi
├── com.squareup.okhttp3 (HTTP)
│   └── logging-interceptor
├── com.squareup.moshi (JSON)
├── org.jetbrains.kotlinx:kotlinx-coroutines (Async)
└── com.jakewharton.timber (Logging)
```

---

## 5. Configuration Requirements

### 5.1 Android Permissions

#### Required Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.READ_CALL_LOG" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ANSWER_PHONE_CALLS" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.WRITE_CONTACTS" />
<uses-permission android:name="android.permission.RECEIVE_SMS" />
<uses-permission android:name="android.permission.READ_SMS" />
<uses-permission android:name="android.permission.SEND_SMS" />
<uses-permission android:name="android.permission.RECEIVE_MMS" />
<uses-permission android:name="android.permission.RECEIVE_WAP_PUSH" />
```

#### Required Hardware Features
```xml
<uses-feature android:name="android.hardware.telephony" android:required="true" />
```

### 5.2 System Roles
| Role | Purpose | Request Method |
|------|---------|----------------|
| Call Screening | Intercept incoming calls | `RoleManager.ROLE_CALL_SCREENING` |
| Default SMS App | Intercept SMS messages | `Telephony.Sms.getDefaultSmsPackage()` |

### 5.3 API Configuration

#### NumLookup API
- Endpoint: `https://api.numlookup.com/`
- Authentication: API Key header
- Required: API key from dashboard

#### Abstract API
- Endpoint: `https://phonevalidation.abstractapi.com/`
- Authentication: API Key parameter
- Required: API key from account

#### HuggingFace Inference API
- Endpoint: `https://api-inference.huggingface.co/models/{model_id}`
- Authentication: Bearer token
- Required: API key + Model ID (e.g., `distilbert-base-uncased`)

#### Custom Endpoint
- Configurable URL, header name, and header value
- User-defined format

### 5.4 Build Configuration

#### Signing Configuration (CI/CD)
```kotlin
signingConfigs {
    create("release") {
        // From signing.properties or environment variables
        storeFile = file(STORE_FILE)
        storePassword = STORE_PASSWORD
        keyAlias = KEY_ALIAS
        keyPassword = KEY_PASSWORD
    }
}
```

#### Environment Variables (Bitrise/Codemagic)
| Variable | Purpose |
|----------|---------|
| `BITRISEIO_ANDROID_KEYSTORE_URL` | Keystore file path |
| `BITRISEIO_ANDROID_KEYSTORE_PASSWORD` | Store password |
| `BITRISEIO_ANDROID_KEYSTORE_ALIAS` | Key alias |
| `BITRISEIO_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD` | Key password |

### 5.5 ProGuard Configuration
```
# Enabled for release builds
isMinifyEnabled = true
proguardFiles(
    getDefaultProguardFile("proguard-android-optimize.txt"),
    "proguard-rules.pro"
)
```

---

## 6. Code Review Findings

### 6.1 Critical Issues

#### Issue 1: Incomplete Call Screening Implementation
**Location:** `service/CallGuardianCallScreeningService.kt:30-37`
**Severity:** HIGH
**Description:** The call screening service doesn't actually perform lookup before responding. It immediately allows the call and then performs notification.
```kotlin
// Current (problematic)
respondToCall(callDetails, android.telecom.CallResponse.Builder().setDisallowCall(false).build())
notificationManager.notifyBlockedCall(normalized, null)
```
**Impact:** All calls are allowed regardless of spam status.
**Recommendation:** Perform lookup before responding, implement timeout-based decision.

#### Issue 2: Missing Error Handling in Coroutine Scopes
**Location:** Multiple service classes
**Severity:** MEDIUM
**Description:** `SmsReceiver` and `CallGuardianCallScreeningService` launch coroutines without exception handlers.
**Impact:** Uncaught exceptions may crash the app or fail silently.
**Recommendation:** Add `CoroutineExceptionHandler` to service scopes.

#### Issue 3: Repository Method Not Implemented
**Location:** `ui/MainViewModel.kt:100`
**Severity:** MEDIUM
**Description:** `analyzeContactSync` and `applyContactSyncChanges` are called on repository but not visible in `LookupRepository.kt`.
**Impact:** Contact sync feature may not work.
**Recommendation:** Implement missing repository methods or verify they exist.

### 6.2 Security Concerns

#### Concern 1: Encryption Fallback to Plain Text
**Location:** `data/settings/LookupPreferences.kt:66,79,93,110`
**Severity:** MEDIUM
**Description:** API keys fall back to unencrypted storage if encryption fails.
**Recommendation:** Fail securely rather than falling back to plain text storage.

#### Concern 2: No Input Sanitization for Phone Numbers
**Location:** Multiple files
**Severity:** LOW
**Description:** Phone numbers are only filtered for digits and `+`, no length validation.
**Recommendation:** Add phone number format validation.

### 6.3 Performance Issues

#### Issue 1: Sequential Await in Parallel Lookup
**Location:** `lookup/ReverseLookupManager.kt:103-105`
**Severity:** MEDIUM
**Description:** Uses `forEach { await() }` which waits sequentially.
**Impact:** Doesn't truly return first available result.
**Recommendation:** Use `select` or `Flow.first()` for true first-result semantics.

#### Issue 2: Missing Database Indexes
**Location:** `data/db/PhoneProfileEntity.kt`, `data/db/PhoneInteractionEntity.kt`
**Severity:** LOW
**Description:** No indexes on `phone_number` in interactions table.
**Impact:** Slower queries for interaction history by phone number.
**Recommendation:** Add `@Index` annotations for frequently queried columns.

### 6.4 Code Quality Issues

#### Issue 1: Unused Timeout Constant
**Location:** `service/CallGuardianCallScreeningService.kt:46`
```kotlin
private const val LOOKUP_TIMEOUT_MS = 2500L
```
**Description:** Defined but never used.

#### Issue 2: ServiceException Import Missing
**Location:** `data/repository/LookupRepository.kt:162`
**Description:** `ServiceException` is referenced but not imported.

#### Issue 3: LookupOutcome Model Incomplete
**Location:** `model/DataModels.kt:39-42`
**Description:** `LookupOutcome` doesn't include `aiAssessment` but `SmsReceiver` accesses it.

---

## 7. Security Considerations

### 7.1 Data Protection
| Data Type | Protection Level | Storage |
|-----------|-----------------|---------|
| API Keys | Encrypted | EncryptedDataStore |
| Phone Numbers | Plain | Room Database |
| Message Bodies | Plain | Room Database |
| Lookup Results | Plain | Room Database + Memory |

### 7.2 Permission Model
- All sensitive permissions requested at runtime
- Clear rationale messages defined in strings.xml
- Permission denial handled gracefully

### 7.3 Network Security
- HTTPS enforced for all API calls
- No certificate pinning (recommended to add)
- API keys transmitted via headers (not URL parameters for most APIs)

### 7.4 Recommendations
1. Add certificate pinning for API endpoints
2. Implement API key rotation mechanism
3. Add database encryption for sensitive fields
4. Remove plain text fallback for encryption failures
5. Add request signing for custom endpoints

---

## 8. Recommendations

### 8.1 High Priority
1. **Complete Call Screening Logic** - Implement actual lookup before call response
2. **Add Coroutine Error Handlers** - Prevent silent failures in services
3. **Implement Missing Repository Methods** - `analyzeContactSync`, `applyContactSyncChanges`
4. **Fix Parallel Lookup** - Use `select` for true first-result semantics
5. **Remove Encryption Fallback** - Fail securely on crypto errors

### 8.2 Medium Priority
1. **Add Database Indexes** - Improve query performance
2. **Implement Database Migrations** - Prepare for schema changes
3. **Add Phone Number Validation** - Format and length checks
4. **Implement Cache Persistence** - Survive process death
5. **Add Rate Limiting** - Prevent API abuse

### 8.3 Low Priority
1. **Add Certificate Pinning** - Enhanced network security
2. **Implement Health Check** - Actually ping sources in periodic checks
3. **Add Metrics Decay** - Reset old health metrics
4. **Improve Memory Estimation** - More accurate cache size calculation
5. **Add Notification ID Collision Handling** - Use better unique IDs

### 8.4 Documentation Needed
1. API endpoint documentation
2. Database migration guide
3. Permission rationale documentation
4. Error code reference
5. Build and deployment guide

---

## Appendix A: File Structure

```
app/src/main/java/com/example/callguardian/
├── CallGuardianApplication.kt
├── MainActivity.kt
├── ai/
│   └── AiRiskScorer.kt
├── data/
│   ├── cache/
│   │   ├── DataFreshnessManager.kt
│   │   └── LookupCacheManager.kt
│   ├── db/
│   │   ├── CallGuardianDatabase.kt
│   │   ├── Converters.kt
│   │   ├── PhoneInteractionDao.kt
│   │   ├── PhoneInteractionEntity.kt
│   │   ├── PhoneProfileDao.kt
│   │   └── PhoneProfileEntity.kt
│   ├── repository/
│   │   └── LookupRepository.kt
│   └── settings/
│       ├── EncryptedDataStoreFactory.kt
│       ├── EncryptedPreferences.kt
│       └── LookupPreferences.kt
├── di/
│   ├── DatabaseModule.kt
│   ├── DispatcherModule.kt
│   ├── LookupModule.kt
│   └── NetworkModule.kt
├── lookup/
│   ├── PerformanceOptimizer.kt
│   ├── ReverseLookupManager.kt
│   ├── ReverseLookupSource.kt
│   ├── ServiceHealthMonitor.kt
│   └── sources/
│       ├── AbstractApiSource.kt
│       ├── CustomEndpointLookupSource.kt
│       └── NumLookupApiSource.kt
├── model/
│   └── DataModels.kt
├── notifications/
│   ├── LookupNotificationManager.kt
│   └── NotificationActionReceiver.kt
├── service/
│   ├── CallGuardianCallScreeningService.kt
│   ├── ContactInfo.kt
│   ├── ContactLookupService.kt
│   └── ContactSyncService.kt
├── sms/
│   └── SmsReceiver.kt
├── ui/
│   ├── CallGuardianApp.kt
│   ├── ContactSyncDialogFragment.kt
│   ├── ContactSyncUiState.kt
│   ├── MainUiState.kt
│   ├── MainViewModel.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── util/
    ├── PermissionUtils.kt
    ├── database/
    │   └── DatabaseManager.kt
    ├── exceptions/
    │   └── CallGuardianException.kt
    ├── handling/
    │   └── ErrorHandling.kt
    ├── logging/
    │   ├── Logger.kt
    │   └── LoggingConfig.kt
    └── network/
        └── NetworkManager.kt
```

---

## Appendix B: Test Coverage

| Component | Unit Tests | Integration Tests | Coverage |
|-----------|------------|-------------------|----------|
| LookupRepository | Yes | Yes | Good |
| ReverseLookupManager | Yes | No | Moderate |
| LookupCacheManager | Partial | No | Low |
| AiRiskScorer | No | No | None |
| CallScreeningService | No | No | None |
| SmsReceiver | No | No | None |
| NotificationManager | Yes | No | Moderate |
| Database DAOs | Yes | Yes | Good |
| EncryptedPreferences | Yes | No | Moderate |

---

*Document generated: 2026-01-13*
*Version: 1.0*
