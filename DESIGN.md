# Vibejournal Android App Design Document

## Overview

Vibejournal is an Android app that transforms spoken thoughts into coherent journal entries, seamlessly integrating with Google Docs. Users connect the app to a specific Google Doc, which serves as their personal journal. By tapping a prominent record button, users can dictate their thoughts, which are then transcribed and refined by a Large Language Model (LLM) such as GPT-4.1. The final entry is automatically saved to their journal with a timestamp.
Vibejournal is an Android app that transforms spoken thoughts into coherent journal entries, seamlessly integrating with Google Docs. Users connect the app to a specific Google Doc, which serves as their personal journal. By tapping a prominent record button, users can dictate their thoughts, which are then transcribed using OpenAI's speech-to-text service and refined by a Large Language Model (LLM) such as GPT-4.1. The final entry is automatically saved to their journal with a timestamp.
---

## Features

- **Google Docs Integration**
  - OAuth authentication for Google account access.
  - Select or create a Google Doc to serve as the journal.
  - Read and write permissions for the chosen document.

- **Voice Recording**
  - Large, easily accessible red "Record" button.
  - Unlimited recording duration.
  - Pause, resume, and stop controls.

- **AI-Powered Journal Entry**
  - Audio is transcribed to text using OpenAI's speech-to-text service.
  - Transcription sent to GPT-4.1 (or similar LLM) for summarization and refinement.
  - Resulting journal entry is formatted and timestamped.

- **Automatic Saving**
  - Entry is appended to the connected Google Doc.
  - Each entry includes the date and time.

- **User Experience**
  - Minimalist, distraction-free interface.
  - Quick access to recent entries.
  - Option to manually edit entries before saving.

---

## Architecture

### 1. Frontend (Android App)
- Kotlin/Java (Android Studio)
- UI Components:
  - Record button
  - Journal selection screen
  - Entry preview and edit screen
- Google Sign-In and Docs API integration

### 2. Backend Services
  - Speech-to-text service (OpenAI Whisper or similar)
  - LLM API (GPT-4.1 via OpenAI or Azure endpoint)
- Secure token management for Google and LMM APIs

### 3. Data Flow

1. **User Authentication**
   - User signs in with Google.
   - App requests access to Google Docs.

2. **Journal Selection**
   - User selects or creates a journal doc.

3. **Recording**
   - User taps record, speaks, and stops recording.

4. **Transcription**
   - Audio sent to speech-to-text service.

5. **AI Processing**
   - Transcribed text sent to LMM for summarization.

6. **Saving**
   - Final entry appended to Google Doc with timestamp.

---

## Key Technologies

- Android SDK (Kotlin/Java)
- Google Docs API
- Google OAuth2
- Speech-to-Text API
- GPT-4.1 (OpenAI/Azure)
- Retrofit/Volley for network requests

---

## Security & Privacy

- All authentication via OAuth2.
- User data (audio, text) processed securely.
- Option to delete recordings after processing.
- No local storage of sensitive data.

---

## Future Enhancements

- Support for multiple journals.
- Entry tagging and search.
- Offline recording and later sync.
- Customizable AI writing styles.

---

## UI Mockups

- **Home Screen:** Big red record button, journal name, recent entries.
- **Journal Selection:** List of Google Docs, create new doc.
- **Entry Preview:** AI-generated entry, edit/save options.

---

## Phases of Implementation

### Phase 1: MVP
- Google authentication and Docs integration
- Select or create a journal doc
- Record audio and transcribe using OpenAI's speech-to-text
- Send transcription to GPT-4.1 (LLM) for journal entry generation
- Append entry to Google Doc with timestamp
- Simple, clean UI with record button and journal selection

### Phase 2: Usability & Polish
- Entry preview and manual editing before saving
- Recent entries view
- Improved error handling and feedback
- UI/UX refinements

### Phase 3: Advanced Features
- Support for multiple journals
- Entry tagging and search
- Offline recording and later sync
- Customizable AI writing styles
- Enhanced privacy controls

### Future Phases
- Integration with other cloud storage providers
- Social sharing features
- Analytics and journaling insights

---

## Conclusion

Vibejournal offers a frictionless way to capture and organize thoughts using voice and AI, leveraging Google Docs for storage and GPT-4.1 for intelligent writing. The app focuses on simplicity, privacy, and seamless integration.

---
