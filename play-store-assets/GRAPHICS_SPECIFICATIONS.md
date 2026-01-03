# CallGuardian Play Store Graphics Specifications

This document contains specifications for creating all required Google Play Store graphics assets.

## Required Graphics Assets

### 1. High-Resolution App Icon (512x512 px)

**File**: `app_icon_512.png`
**Dimensions**: 512 x 512 pixels
**Format**: 32-bit PNG (with alpha)
**Max file size**: 1024 KB

**Design Specifications**:
```
Background: Circular gradient
- Primary: #005AC1 (CallGuardian Blue)
- Secondary: #003D82 (Dark Blue accent)

Foreground Elements:
- Phone device outline: #FFFFFF
- Phone screen: #E3F2FD (Light Blue)
- Signal bars: #90CAF9 (Medium Blue)
- Checkmark/Shield: #4CAF50 (Success Green)

Safe Zone: Content should be within 66% of icon area (inner 340x340px)
Visual Weight: Centered, balanced composition
```

**Dark Mode Variant** (for promotional materials):
```
Background: #0F1419 (Dark Slate)
Foreground: #A8C8FF (Light Blue)
Accent: #81C784 (Soft Green)
```

---

### 2. Feature Graphic (1024x500 px)

**File**: `feature_graphic.png` / `feature_graphic_dark.png`
**Dimensions**: 1024 x 500 pixels
**Format**: JPG or 24-bit PNG (no alpha)

#### Light Mode Design:
```
Background:
- Gradient: #005AC1 → #003D82 (left to right)
- Subtle wave pattern overlay at 5% opacity

Left Side (60%):
- App Icon (192px) positioned at left center
- Primary text below icon

Center Text:
- Headline: "CallGuardian" (White, Bold, 72px)
- Subheadline: "AI-Powered Call & Message Protection" (White, Regular, 32px)
- Tagline: "Block Spam • Identify Callers • Protect Privacy" (White/90%, Light, 24px)

Right Side (40%):
- Phone mockup showing app interface
- Subtle shadow and depth effect
```

#### Dark Mode Design:
```
Background:
- Solid: #0F1419 with subtle gradient to #1A2633
- Accent glow effect at edges

Colors:
- Primary Text: #A8C8FF
- Secondary Text: #DDE3EA
- Accent: #81C784

Same layout as light mode with inverted color scheme
```

---

### 3. Phone Screenshots (1080x1920 px or 1080x2340 px for tall displays)

**Format**: JPG or 24-bit PNG
**Required**: Minimum 2, Maximum 8 screenshots

#### Screenshot 1: Main Dashboard
```
Content: Home screen showing recent calls with AI risk scores
Elements to highlight:
- Recent call list with colored risk indicators
- Quick action buttons (Block, Allow)
- AI assessment badges (Safe/Warning/Danger)
- Clean Material Design 3 interface

Frame: Device mockup (Pixel 8 style)
Background: Subtle gradient matching app theme
Caption: "Smart Dashboard with AI Protection"
```

#### Screenshot 2: Caller Lookup Results
```
Content: Detailed caller information screen
Elements to highlight:
- Caller name/business identification
- Carrier and location info
- AI spam probability score with confidence
- Block mode toggle options
- One-tap save to contacts

Caption: "Instant Caller Identification"
```

#### Screenshot 3: Settings & Configuration
```
Content: Settings screen showing lookup sources
Elements to highlight:
- Multiple API source configuration
- AI integration toggle
- Privacy settings
- Block mode preferences

Caption: "Fully Customizable Protection"
```

#### Screenshot 4: Notification Interface
```
Content: Notification showing incoming call screening
Elements to highlight:
- Caller preview information
- Quick action buttons
- AI risk assessment
- One-tap block/allow/save options

Caption: "Quick Actions from Notifications"
```

#### Screenshot 5: Message Filtering
```
Content: SMS screening interface
Elements to highlight:
- Message list with spam indicators
- AI-detected spam messages
- Safe sender indicators
- Quick block actions

Caption: "SMS & Message Protection"
```

#### Screenshot 6 (Optional): Contact Sync
```
Content: Contact management interface
Elements to highlight:
- Trusted contacts list
- Sync status indicators
- Quick add from call history

Caption: "Manage Trusted Contacts"
```

---

### 4. Tablet Screenshots (Optional but recommended)

**Dimensions**:
- 7-inch: 1200x1920 px
- 10-inch: 1600x2560 px or 2048x2732 px

Same content as phone screenshots, adapted for tablet layout.

---

### 5. Promotional Graphics (Optional)

#### Promo Video Thumbnail (1280x720 px)
```
Layout: Similar to feature graphic
Include: App icon, headline, and "Watch Demo" overlay
```

#### TV Banner (for Android TV, if applicable) (1280x720 px)
```
Note: Not required for phone-only apps
```

---

## Color Palette Reference

### Light Theme
| Color Name | Hex Code | Usage |
|------------|----------|-------|
| Primary Blue | #005AC1 | Main brand color, buttons |
| Dark Blue | #003D82 | Accents, gradients |
| Light Blue | #E3F2FD | Backgrounds, cards |
| Accent Blue | #90CAF9 | Icons, indicators |
| Success Green | #4CAF50 | Safe indicators, checkmarks |
| Warning Orange | #FF9800 | Medium risk indicators |
| Danger Red | #F44336 | High risk, spam indicators |
| White | #FFFFFF | Text, foreground elements |
| Surface | #FAFCFF | App background |

### Dark Theme
| Color Name | Hex Code | Usage |
|------------|----------|-------|
| Primary Blue | #A8C8FF | Main brand color |
| Dark Background | #0F1419 | App background |
| Surface Dark | #1A2633 | Cards, elevated surfaces |
| Accent Blue | #004A77 | Container backgrounds |
| Success Green | #81C784 | Safe indicators |
| Warning Orange | #FFB74D | Medium risk indicators |
| Danger Red | #EF5350 | High risk indicators |
| Text Primary | #DDE3EA | Main text |
| Text Secondary | #8D9199 | Secondary text |

---

## Typography

**Primary Font**: Roboto (Google Fonts)
- Headlines: Roboto Bold
- Body: Roboto Regular
- Captions: Roboto Light

**Fallback**: System default sans-serif

---

## Asset Checklist

- [ ] app_icon_512.png (512x512)
- [ ] app_icon_512_dark.png (512x512, dark variant for marketing)
- [ ] feature_graphic.png (1024x500, light mode)
- [ ] feature_graphic_dark.png (1024x500, dark mode)
- [ ] screenshot_01_dashboard.png (1080x1920+)
- [ ] screenshot_02_lookup.png (1080x1920+)
- [ ] screenshot_03_settings.png (1080x1920+)
- [ ] screenshot_04_notification.png (1080x1920+)
- [ ] screenshot_05_sms.png (1080x1920+)
- [ ] screenshot_06_contacts.png (1080x1920+, optional)
- [ ] promo_video_thumbnail.png (1280x720, optional)

---

## Export Settings

### For PNG files:
- Color depth: 32-bit (with alpha for icons)
- Compression: Maximum quality
- Color profile: sRGB

### For JPG files:
- Quality: 90-95%
- Color profile: sRGB

---

## Tools Recommended

1. **Figma** - UI design and mockups
2. **Adobe Illustrator** - Vector icon creation
3. **Android Asset Studio** - Icon generation
4. **Sketch** - Alternative design tool
5. **Photoshop** - Raster graphics and export

---

## Notes

- All text should be localized for different markets
- Screenshots should show realistic, non-personal data
- Ensure all graphics are free from copyrighted material
- Test visibility on various screen sizes and resolutions
- Google Play Console will preview how assets appear on store listing
