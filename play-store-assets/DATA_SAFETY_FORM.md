# CallGuardian - Google Play Data Safety Form

This document provides the exact answers for the Google Play Console Data Safety section.

---

## Overview

**Does your app collect or share any of the required user data types?**
> Yes

**Is all of the user data collected by your app encrypted in transit?**
> Yes

**Do you provide a way for users to request that their data be deleted?**
> Yes

---

## Data Types Collected

### 1. Phone Numbers

**Collected**: Yes
**Shared**: No (only sent to configured lookup APIs with user consent)
**Purpose**: App functionality (call screening and caller identification)
**Required**: Yes (core app functionality)
**User Choice**: Cannot opt out (required for core functionality)

---

### 2. Call Logs

**Collected**: Yes
**Shared**: No
**Purpose**: App functionality (call history and screening)
**Required**: Yes
**User Choice**: Cannot opt out (required for core functionality)

---

### 3. SMS/MMS Messages

**Collected**: Yes
**Shared**: No
**Purpose**: App functionality (message filtering)
**Required**: Yes
**User Choice**: Cannot opt out (required for message filtering)

---

### 4. Contacts

**Collected**: Yes
**Shared**: No
**Purpose**: App functionality (contact matching and trusted number identification)
**Required**: No (optional permission)
**User Choice**: Can choose not to grant permission

---

### 5. Device or Other IDs

**Collected**: No
**Note**: App does not collect device advertising IDs or other device identifiers

---

### 6. App Activity

**Collected**: Limited (feature usage for local statistics only)
**Shared**: No
**Purpose**: Analytics (app performance)
**Required**: No
**Storage**: Local only, not transmitted

---

## Data Handling Practices

### Data Encryption

| Data Type | Encrypted in Transit | Encrypted at Rest |
|-----------|---------------------|-------------------|
| Phone Numbers | Yes (HTTPS) | Yes (EncryptedSharedPreferences) |
| Call Logs | N/A (local only) | Yes |
| SMS/MMS | N/A (local only) | Yes |
| Contacts | N/A (local only) | No (system managed) |
| API Keys | Yes | Yes (AES-256) |

### Data Deletion

Users can delete their data via:
1. App Settings → Privacy → Delete All Data
2. Uninstalling the app (all local data is deleted)

### Data Retention

| Data Type | Retention Period |
|-----------|-----------------|
| Call/Message Logs | 90 days (configurable) |
| Lookup Cache | 30 days |
| Contact Data | Until deletion or uninstall |
| Preferences | Until deletion or uninstall |

---

## Third-Party Data Sharing

### API Services Used

#### 1. NumLookup API
- **Data Sent**: Phone numbers (for lookup)
- **Purpose**: Caller identification
- **User Control**: Can be disabled in settings
- **Privacy Policy**: https://numlookupapi.com/privacy

#### 2. Abstract API
- **Data Sent**: Phone numbers (for validation)
- **Purpose**: Phone number validation and carrier info
- **User Control**: Can be disabled in settings
- **Privacy Policy**: https://www.abstractapi.com/privacy-policy

#### 3. Hugging Face Inference API (Optional)
- **Data Sent**: Anonymized call metadata (for AI analysis)
- **Purpose**: AI-powered spam detection
- **User Control**: Disabled by default, opt-in only
- **Privacy Policy**: https://huggingface.co/privacy

---

## Play Console Form Answers

### Section: Data Collection and Security

**Q: Does your app collect any user data?**
> Yes

**Q: Is all data collected encrypted in transit?**
> Yes

**Q: Do you provide a way for users to request data deletion?**
> Yes

---

### Section: Data Types - Location

**Q: Does your app collect Approximate location?**
> No

**Q: Does your app collect Precise location?**
> No

---

### Section: Data Types - Personal Info

**Q: Does your app collect Name?**
> No (only accessed from contacts, not collected)

**Q: Does your app collect Email address?**
> No

**Q: Does your app collect User IDs?**
> No

**Q: Does your app collect Address?**
> No

**Q: Does your app collect Phone number?**
> Yes
- Purpose: App functionality
- Shared: No (sent to lookup APIs for functionality)
- Collected: Yes

---

### Section: Data Types - Messages

**Q: Does your app collect Emails?**
> No

**Q: Does your app collect SMS or MMS?**
> Yes
- Purpose: App functionality (spam filtering)
- Shared: No
- Collected: Yes (stored locally)

**Q: Does your app collect Other in-app messages?**
> No

---

### Section: Data Types - Contacts

**Q: Does your app collect Contacts?**
> Yes
- Purpose: App functionality (trusted number identification)
- Shared: No
- Collected: Yes (accessed locally)

---

### Section: Data Types - App Activity

**Q: Does your app collect App interactions?**
> No (no analytics transmitted)

**Q: Does your app collect In-app search history?**
> No

**Q: Does your app collect Installed apps?**
> No

**Q: Does your app collect Other user-generated content?**
> No

---

### Section: Data Types - Device or Other IDs

**Q: Does your app collect Device or other IDs?**
> No

---

### Section: Data Sharing

**Q: Does your app share any data with third parties?**
> Yes (phone numbers sent to lookup APIs for functionality)

**Shared data details:**
- Data type: Phone numbers
- Recipient: Third-party lookup API services
- Purpose: App functionality (caller identification)
- User initiated: Yes (when lookup is triggered)

---

### Section: Data Security

**Q: Is data transferred over a secure connection?**
> Yes (all API calls use HTTPS/TLS)

**Q: Can users request data deletion?**
> Yes (via app settings or by uninstalling)

**Q: Committed to Play Families Policy?**
> Not applicable (app is not designed for children)

---

## Declaration Statement

CallGuardian is committed to user privacy and data protection. All data collection serves the core functionality of call and message screening. No data is sold to third parties, and users maintain full control over their information.

**Last Updated**: January 3, 2026
