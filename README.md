# Ham Radio Logger - Android App

A professional Android application for amateur radio (ham radio) operators to log QSOs with direct QRZ.com integration, band prediction, GPS grid square calculation, and offline logbook functionality.

## Key Features

✅ **QRZ Integration** - Direct login & upload contacts to QRZ.com
✅ **GPS Location** - Auto-capture location and calculate Maidenhead grid squares
✅ **Band Predictor** - Solar flux, K-Index, sunspot analysis for band conditions
✅ **Offline Logbook** - SQLite database with local storage
✅ **Time Tracking** - UTC time-on and time-off recording
✅ **Export Options** - CSV and ADIF format support
✅ **Auto-Upload** - Automatic QRZ sync on save

## Build Status

**Current Development Stage:** Core implementation complete

- ✅ Database layer (Room/SQLite)
- ✅ Network integration (Retrofit/OkHttp)
- ✅ QRZ API client
- ✅ Band prediction logic
- ✅ GPS/Location handling
- ✅ UI screens (Jetpack Compose)
- ✅ ViewModels & state management
- ⏳ Testing & QA (in progress)
- ⏳ Build optimization & APK generation

## Installation & Download

### When can you download the APK?

**Timeline:**
- **Week 1 (by Sept 8):** Alpha APK - Internal testing version
- **Week 2 (by Sept 15):** Beta APK - Feature-complete, initial bug fixes
- **Week 3 (by Sept 22):** Release Candidate - Ready for download

**Download Methods:**
1. **GitHub Releases** - https://github.com/suyog-vu3ohf/ham-radio-logger/releases
2. **Direct APK** - Once tested, available in Releases section
3. **Google Play Store** - Coming later (requires app review)

### How to Install

```bash
# Download APK from releases
# Enable "Unknown Sources" in Android Settings
# Tap APK file to install
```

Minimum Android: 7.0 (API 24)
Target Android: 14+ (API 34)

## Development Setup

### Prerequisites
- Android Studio Giraffe or later
- JDK 17+
- Git

### Clone & Build

```bash
git clone https://github.com/suyog-vu3ohf/ham-radio-logger.git
cd ham-radio-logger
./gradlew build
```

### Run on Emulator/Device

```bash
./gradlew installDebug
```

## Features Overview

### 1. Contact Logging
- Call sign entry (auto-uppercase)
- Frequency in MHz
- Mode selection (SSB, CW, FM, PSK, RTTY, FT8, JT65)
- UTC time-on and time-off
- Signal report (RST format)
- Notes field
- Optional GPS location capture

### 2. QRZ Integration

**Login & Authentication:**
```
Home Screen → QRZ Upload → Enter credentials
```

**Automatic Upload:**
- Save contact → Auto-upload if enabled
- Settings → Toggle "Auto Upload to QRZ"
- Tracks upload status per contact

**Credentials Stored Securely:**
- Android Keystore encryption
- Never stored in plain text

### 3. GPS & Grid Squares

**How it works:**
1. Toggle GPS in New Contact screen
2. App requests location permission
3. Captures latitude/longitude
4. Auto-calculates Maidenhead grid square
5. Shows in logbook as "Grid: FN31pr" format

**Grid Square Calculation:**
- 4-character format (2 letters + 2 digits)
- Used for QRZ lookup and propagation estimates

### 4. Band Predictor

**Solar Data:**
- Solar Flux Index (SFI) - 0-300
- K-Index - 0-9 (geomagnetic activity)
- Sunspot count - 0-200

**Predictions:**
- Shows open bands with confidence %
- Highlights optimal conditions
- Adjusts for your latitude
- Updates based on real-time solar data

**Open Bands by Conditions:**

| Condition | Flux | Bands | Best Time |
|-----------|------|-------|----------|
| Extreme | >150 | 10/12/15/17/20m | Daytime |
| Active | 101-150 | 10/15/20/40m | Mixed |
| Normal | 70-100 | 20/40/80m | All |
| Quiet | <70 | 40/80/160m | Night |

### 5. Offline Logbook

**Database Schema:**
```sql
radio_contacts (
  id (PRIMARY KEY)
  callSign TEXT
  frequency REAL (MHz)
  mode TEXT (SSB, CW, etc.)
  timeOn DATETIME (UTC)
  timeOff DATETIME (UTC, nullable)
  signalReport TEXT (5/9, etc.)
  notes TEXT
  latitude REAL (nullable)
  longitude REAL (nullable)
  uploadedToQRZ BOOLEAN
  createdAt DATETIME
)
```

**Features:**
- No internet required
- Auto-sync when online
- Backup/restore capability

### 6. Export & Backup

**CSV Export:**
```
Call Sign,Frequency,Mode,Time On,Time Off,Signal Report,Notes,Grid
W5XYZ,14.265,SSB,2024-09-01 14:30,2024-09-01 14:45,5/9,Nice signal,FN31pr
```

**ADIF Export:** (Compatible with loggers, eQSL, ARRL)
```
<CALL:5>W5XYZ
<FREQ:8>14.26500
<MODE:3>SSB
<QSO_DATE:8>20240901
<TIME_ON:6>143000
<RST_SENT:3>5/9
<EOR>
```

## API Integration

### QRZ.com
**Endpoint:** `https://xmlrpc.qrz.com/xmlrpc.php`

**Methods:**
- `login(username, password)` → Returns session key
- `insertLogEntry(key, call, qso_date, time_on, ...)` → Uploads QSO

### Band Data
**Endpoint:** `https://www.hamqsl.com/solargmi.php`

Fetches real-time solar data for band predictions

## Permissions Required

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

## Architecture

**Clean Architecture with MVVM:**
```
UI Layer (Compose)
    ↓
ViewModel (State Management)
    ↓
Use Cases (Business Logic)
    ↓
Repository (Data Abstraction)
    ↓
Data Layer (Network + Local DB)
```

**Tech Stack:**
- Language: Kotlin
- UI: Jetpack Compose + Material Design 3
- DB: Room (SQLite)
- Network: Retrofit + OkHttp + GSON
- Location: Google Play Services
- Async: Coroutines + Flow

## Roadmap

- [x] Core logging functionality
- [x] QRZ API integration
- [x] GPS/Grid square calculation
- [x] Band predictor logic
- [x] Offline database
- [ ] eQSL integration
- [ ] CQ/DX spotting network
- [ ] Contest logging mode
- [ ] Antenna calculator
- [ ] Propagation maps
- [ ] Dark mode optimization

## Building APK for Release

### Create Signed APK

```bash
# Generate keystore (one time)
keytool -genkey -v -keystore hamradio.jks -keyalg RSA -keysize 2048 -validity 10000

# Build signed release APK
./gradlew assembleRelease
```

**Output:** `app/build/outputs/apk/release/app-release.apk`

### Expected APK Size
- **Debug APK:** ~45 MB
- **Release APK:** ~28 MB (after ProGuard optimization)

## Testing

### Required Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests (Android device/emulator)
./gradlew connectedAndroidTest
```

### Manual Testing Checklist

- [ ] Log new contact with all fields
- [ ] GPS location capture and grid calculation
- [ ] QRZ login with valid credentials
- [ ] Upload single contact to QRZ
- [ ] View complete logbook
- [ ] Export to CSV
- [ ] Export to ADIF
- [ ] Band predictor with various solar indices
- [ ] Offline operation (flight mode)
- [ ] Auto-sync when reconnected

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

MIT License - See LICENSE file for details

## Contact & Support

- **Email:** vu3ohf@gmail.com
- **QRZ:** https://www.qrz.com/db/VU3OHF
- **GitHub Issues:** https://github.com/suyog-vu3ohf/ham-radio-logger/issues

## Acknowledgments

- QRZ.com for logbook API
- HamQSL for solar data
- Android Jetpack team for Compose framework
- Ham radio community for feedback

---

**Last Updated:** September 1, 2024
**Version:** 1.0.0 (Alpha)
