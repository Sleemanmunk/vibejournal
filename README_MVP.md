# VibeJournal Android App - MVP Implementation

This is the MVP implementation of the VibeJournal Android app that transforms spoken thoughts into coherent journal entries using Google Docs integration and OpenAI's services.

## Features (MVP)

- ✅ Google authentication and Docs integration
- ✅ Select or create a journal doc
- ✅ Record audio and transcribe using OpenAI's speech-to-text
- ✅ Send transcription to GPT-4 for journal entry generation
- ✅ Append entry to Google Doc with timestamp
- ✅ Simple, clean UI with record button and journal selection

## Setup Instructions

### 1. Google Cloud Console Setup

**Use the detailed step-by-step guide in `GOOGLE_SETUP_GUIDE.md` - it has comprehensive instructions!**

**Quick steps:**
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable the following APIs:
   - Google Docs API
   - Google Drive API
4. Create credentials (OAuth 2.0 Client ID for Android)
5. **Your SHA-1 fingerprint:** `FA:00:CB:71:63:5A:05:9F:CC:1C:8A:B1:B5:76:14:60:CA:EE:01:97`
6. Download the `google-services.json` file (NOT the client_secret file)
7. Replace the placeholder in `app/google-services.json`

**Note:** The file you found (`client_secret_651397731003-m6ifpb8hrrojt0v6n1ho5vldjcgep7bv.apps.googleusercontent.com`) is for web apps. You need the Android-specific `google-services.json` file.

### 2. OpenAI API Setup

1. Get your OpenAI API key from [OpenAI Platform](https://platform.openai.com/)
2. Replace `your-openai-api-key-here` in `AudioProcessingService.kt` with your actual API key
3. **Important**: Move the API key to secure storage (Android Keystore) for production

### 3. Android Studio Setup

1. Open Android Studio
2. Open this project
3. Wait for Gradle sync to complete
4. Connect an Android device or start an emulator
5. Run the app

### 4. SHA-1 Certificate Setup

1. Generate SHA-1 fingerprint:
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```
2. Add the SHA-1 fingerprint to your Google Cloud Console OAuth client
3. Update the `google-services.json` file

## File Structure

```
app/
├── build.gradle                          # App-level dependencies
├── google-services.json                  # Google services configuration
├── src/main/
    ├── AndroidManifest.xml               # App permissions and activities
    ├── java/com/vibejournal/app/
    │   ├── ui/
    │   │   ├── MainActivity.kt            # Main screen with record button
    │   │   └── JournalSelectionActivity.kt # Journal selection screen
    │   ├── viewmodel/
    │   │   ├── MainViewModel.kt           # Main screen logic
    │   │   └── JournalSelectionViewModel.kt # Journal selection logic
    │   ├── service/
    │   │   ├── GoogleDocsService.kt       # Google Docs API integration
    │   │   └── AudioProcessingService.kt  # OpenAI API integration
    │   ├── model/
    │   │   └── GoogleDoc.kt               # Data model for documents
    │   └── adapter/
    │       └── JournalAdapter.kt          # RecyclerView adapter
    └── res/
        ├── layout/                        # UI layouts
        ├── values/                        # Colors, strings, themes
        ├── drawable/                      # Icons
        └── xml/                          # Configuration files
```

## Usage

1. **Sign In**: Tap "Sign in with Google" and authorize the app
2. **Select Journal**: Tap "Select Journal" to choose or create a Google Doc
3. **Record**: Tap the red record button to start recording your thoughts
4. **Stop**: Tap again to stop recording and process the audio
5. **Wait**: The app will transcribe, generate a journal entry, and save it to your Google Doc

## Security Notes

- **API Keys**: Move OpenAI API key to Android Keystore for production
- **Permissions**: App requests microphone and storage permissions
- **OAuth**: Uses Google OAuth2 for secure authentication
- **Data**: Audio files are deleted after processing

## Dependencies

- **Google Play Services**: Authentication and Google APIs
- **Google APIs**: Docs and Drive integration
- **Retrofit/OkHttp**: Network requests to OpenAI
- **Material Design**: UI components
- **EasyPermissions**: Permission handling

## Known Limitations (MVP)

- No offline recording capability
- No entry editing before saving
- No multiple journal support
- API key stored in code (needs secure storage)
- Basic error handling

## Next Steps (Phase 2)

- Entry preview and editing
- Improved error handling
- Better UI/UX
- Secure API key storage
- Recent entries view

## Support

For issues or questions, please refer to the design document or create an issue in the repository.
