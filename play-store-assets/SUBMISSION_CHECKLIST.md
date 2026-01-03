# CallGuardian - Google Play Store Submission Checklist

Complete checklist for submitting CallGuardian to the Google Play Store.

---

## Pre-Submission Requirements

### App Configuration

- [x] **Package Name**: `com.callguardian.app` (production-ready)
- [x] **Version Code**: 1
- [x] **Version Name**: 1.0.0
- [x] **Min SDK**: 29 (Android 10)
- [x] **Target SDK**: 34 (Android 14)
- [ ] **Signing Key**: Generate production keystore
- [ ] **App Bundle**: Generate signed AAB for upload

### Code Quality

- [x] **ProGuard/R8**: Enabled for release builds
- [x] **Debug disabled**: Release build type configured
- [ ] **Testing**: Run full test suite
- [ ] **Lint**: No critical warnings
- [ ] **Crash-free**: Verified stability

---

## Store Listing Assets

### Required Graphics

| Asset | Dimensions | Status | File |
|-------|------------|--------|------|
| High-res icon | 512x512 PNG | [ ] Create | `app_icon_512.png` |
| Feature graphic | 1024x500 PNG/JPG | [ ] Create | `feature_graphic.png` |
| Screenshot 1 | 1080x1920+ | [ ] Create | `screenshot_01.png` |
| Screenshot 2 | 1080x1920+ | [ ] Create | `screenshot_02.png` |

### Store Listing Content

| Content | Max Length | Status | Location |
|---------|------------|--------|----------|
| App title | 30 chars | [x] Done | "CallGuardian" |
| Short description | 80 chars | [x] Done | `STORE_LISTING.md` |
| Full description | 4000 chars | [x] Done | `STORE_LISTING.md` |
| What's new | 500 chars | [x] Done | `STORE_LISTING.md` |

### Design Specifications

- [x] **Icon Specs**: `GRAPHICS_SPECIFICATIONS.md`
- [x] **Screenshot Specs**: `GRAPHICS_SPECIFICATIONS.md`
- [x] **Color Palette**: Defined in specs
- [x] **Dark Mode Variants**: Icon specs included

---

## Google Play Console Setup

### Developer Account

- [ ] Google Play Developer account created ($25 one-time fee)
- [ ] Developer profile completed
- [ ] Payment profile setup (for future monetization)

### App Creation

- [ ] New app created in Play Console
- [ ] App access configured (app is accessible to all)
- [ ] Ads declaration (no ads)

---

## Store Listing Configuration

### Main Store Listing

- [ ] App name: "CallGuardian"
- [ ] Short description uploaded
- [ ] Full description uploaded
- [ ] Screenshots uploaded (min 2)
- [ ] Feature graphic uploaded
- [ ] App icon uploaded

### Contact Details

- [ ] Developer email: `support@callguardian.app`
- [ ] Privacy policy URL (must be hosted publicly)
- [ ] Website URL (optional)

---

## App Content Section

### Privacy Policy

- [x] **Document**: `PRIVACY_POLICY.md`
- [ ] **Hosted URL**: Deploy to public URL
- [ ] **Added to Play Console**: Link in app content section

### Data Safety

- [x] **Form Answers**: `DATA_SAFETY_FORM.md`
- [ ] **Completed in Console**: All questions answered

**Data Collection Summary:**
| Data Type | Collected | Shared | Purpose |
|-----------|-----------|--------|---------|
| Phone numbers | Yes | Yes (lookup APIs) | App functionality |
| Call logs | Yes | No | App functionality |
| SMS/MMS | Yes | No | App functionality |
| Contacts | Yes | No | App functionality |
| Device IDs | No | No | N/A |

### Content Rating

- [x] **Questionnaire Answers**: `CONTENT_RATING_QUESTIONNAIRE.md`
- [ ] **Completed in Console**: IARC rating received

**Expected Rating**: Everyone (IARC 3+)

### Target Audience

- [ ] Target age group: 13+ (not designed for children)
- [ ] Not a teacher-approved app
- [ ] Appeal to children: No

---

## App Releases

### Production Release

- [ ] Countries/regions: All countries (or specify)
- [ ] Release track: Production
- [ ] Release notes: v1.0.0 notes added
- [ ] App bundle uploaded

### Testing Tracks (Recommended)

- [ ] Internal testing: Setup and test with team
- [ ] Closed testing: Beta testers invited
- [ ] Open testing: Public beta (optional)

---

## Policy Compliance

### Sensitive Permissions

The following permissions require declaration:

| Permission | Declaration Status | Reason |
|------------|-------------------|--------|
| READ_CALL_LOG | [ ] Complete | Call screening core functionality |
| READ_PHONE_STATE | [ ] Complete | Call handling |
| RECEIVE_SMS/READ_SMS | [ ] Complete | Message filtering |
| ANSWER_PHONE_CALLS | [ ] Complete | Call blocking |
| WRITE_CONTACTS | [ ] Complete | Save trusted contacts |

### Permission Declaration Form

- [ ] Submitted permissions declaration form
- [ ] Video demonstration uploaded (if required)
- [ ] Use case description provided

### Policy Compliance

- [x] **Permissions Policy**: Core functionality justification documented
- [x] **User Data Policy**: Privacy policy compliant
- [x] **Families Policy**: Not applicable (not for children)
- [ ] **All policies reviewed**: Final compliance check

---

## Technical Requirements

### App Signing

- [ ] Use Google Play App Signing (recommended)
- [ ] Or: Upload your own signing key
- [ ] Keep backup of signing key securely

### App Bundle

- [ ] Build signed Android App Bundle (.aab)
- [ ] Verify bundle with bundletool
- [ ] Test on multiple device configurations

### Build Commands

```bash
# Generate signed release bundle
./gradlew bundleRelease

# Output location
app/build/outputs/bundle/release/app-release.aab
```

---

## Pre-Launch Checklist

### Final Testing

- [ ] Test on Android 10 device
- [ ] Test on Android 14 device
- [ ] Test all core features:
  - [ ] Call screening
  - [ ] SMS filtering
  - [ ] Caller lookup
  - [ ] Blocking functionality
  - [ ] Notifications
  - [ ] Settings
- [ ] Test permissions flow
- [ ] Test dark mode
- [ ] Test accessibility features

### Legal Review

- [x] Privacy policy complete
- [x] Terms of service complete
- [ ] Legal review by attorney (recommended)
- [ ] GDPR compliance verified
- [ ] CCPA compliance verified

---

## Launch Day Tasks

### Submission

1. [ ] Upload final app bundle
2. [ ] Complete all store listing sections
3. [ ] Submit for review
4. [ ] Monitor review status

### Monitoring

- [ ] Set up Google Play Console alerts
- [ ] Monitor crash reports (Firebase/Play Console)
- [ ] Set up review notifications
- [ ] Prepare responses for common user questions

---

## Post-Launch Tasks

### First Week

- [ ] Respond to user reviews
- [ ] Monitor crash-free rate
- [ ] Track download metrics
- [ ] Address any policy warnings

### Ongoing

- [ ] Regular app updates
- [ ] Feature improvements based on feedback
- [ ] Monitor competitor apps
- [ ] Marketing and promotion

---

## Helpful Resources

### Documentation
- [Google Play Console Help](https://support.google.com/googleplay/android-developer)
- [Android App Bundle Guide](https://developer.android.com/guide/app-bundle)
- [Play Store Policies](https://play.google.com/about/developer-content-policy/)

### Tools
- [Android Studio](https://developer.android.com/studio)
- [bundletool](https://github.com/google/bundletool)
- [Play Console](https://play.google.com/console)

---

## Files in This Package

| File | Description |
|------|-------------|
| `GRAPHICS_SPECIFICATIONS.md` | Design specs for all graphics |
| `DATA_SAFETY_FORM.md` | Answers for data safety section |
| `CONTENT_RATING_QUESTIONNAIRE.md` | Content rating answers |
| `STORE_LISTING.md` | Store listing text content |
| `MARKETING_MATERIALS.md` | Marketing and promotional content |
| `SUBMISSION_CHECKLIST.md` | This checklist |

---

**Last Updated**: January 3, 2026

Ready to submit? Complete all items marked with [ ] before publishing to Google Play Store.
