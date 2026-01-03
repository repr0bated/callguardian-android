# Encrypted Storage Implementation

This document describes the implementation of encrypted storage for API keys using AndroidX Security in the CallGuardian Android application.

## Overview

The implementation provides secure storage for sensitive data like API keys while maintaining backward compatibility with existing plain text preferences.

## Components

### 1. Dependencies

Added AndroidX Security dependency to `app/build.gradle.kts`:
```kotlin
implementation("androidx.security:security-crypto:1.1.0-alpha06")
```

### 2. EncryptedDataStoreFactory

Located in `app/src/main/java/com/example/callguardian/data/settings/EncryptedDataStoreFactory.kt`

- Creates encrypted DataStore instances using AndroidX Security
- Uses AES256_GCM encryption for both keys and values
- Handles encryption key generation and management
- Provides proper error handling for encryption failures

### 3. EncryptedPreferences

Located in `app/src/main/java/com/example/callguardian/data/settings/EncryptedPreferences.kt`

Key features:
- **Secure Storage**: All sensitive data is encrypted using AES256
- **Backward Compatibility**: Automatically migrates from plain text to encrypted storage
- **Error Handling**: Graceful fallback to plain text storage if encryption fails
- **Migration**: One-time migration process preserves existing user data

#### Sensitive Data Encrypted:
- NumLookup API Key
- Abstract API Key  
- Custom Endpoint Configuration
- Hugging Face API Credentials

### 4. LookupPreferences Integration

Updated `app/src/main/java/com/example/callguardian/data/settings/LookupPreferences.kt` to:
- Use encrypted storage for sensitive data
- Maintain plain text storage for non-sensitive configuration
- Provide fallback mechanisms for encryption failures
- Combine encrypted and plain text preferences seamlessly

### 5. Dagger Integration

Updated `app/src/main/java/com/example/callguardian/di/LookupModule.kt` to provide EncryptedPreferences dependency injection.

## Security Features

### Encryption Algorithm
- **Key Encryption**: AES256_SIV (Synthetic Initialization Vector)
- **Value Encryption**: AES256_GCM (Galois/Counter Mode)
- **Key Generation**: Android Keystore with AES256_GCM_SPEC

### Error Handling
- Custom `EncryptedPreferencesException` for encryption-specific errors
- Graceful fallback to plain text storage when encryption fails
- Comprehensive logging using Timber for debugging

### Migration Strategy
1. Check if migration has been completed
2. If not, read from plain text storage
3. Write to encrypted storage
4. Mark migration as completed
5. Preserve all existing user data

## Usage

### For API Keys and Sensitive Data:
```kotlin
// Use encrypted storage
encryptedPreferences.updateNumLookupKey(apiKey)
```

### For Non-Sensitive Configuration:
```kotlin
// Use plain text storage (existing behavior)
dataStore.edit { it[Keys.someSetting] = value }
```

## Testing

Comprehensive test suite in `app/src/test/java/com/example/callguardian/data/settings/EncryptedPreferencesTest.kt`:
- Unit tests for all encryption operations
- Migration testing
- Error condition handling
- Backward compatibility verification

## Best Practices Implemented

1. **Separation of Concerns**: Sensitive vs non-sensitive data storage
2. **Graceful Degradation**: Fallback mechanisms for encryption failures
3. **Backward Compatibility**: Seamless migration for existing users
4. **Error Handling**: Comprehensive exception handling and logging
5. **Security**: Industry-standard encryption algorithms
6. **Testing**: Full test coverage for encryption functionality

## Migration Process

When users update to this version:

1. **First Launch**: System detects existing plain text preferences
2. **Migration**: Automatically copies data to encrypted storage
3. **Verification**: Confirms successful migration
4. **Future Use**: All sensitive data stored encrypted

## Future Considerations

- Consider encrypting additional sensitive data as needed
- Monitor for AndroidX Security updates and security patches
- Review encryption key rotation policies if required
- Consider adding user-controlled encryption options