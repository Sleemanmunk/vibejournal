# VibeJournal Setup - Prerequisites

Before you can build and run the VibeJournal Android app, you need to install the necessary development tools.

## Option 1: Install Android Studio (Recommended - Easiest)

**Android Studio includes everything you need:**
- Java Development Kit (JDK)
- Android SDK
- Gradle
- Emulator

### Steps:
1. Download [Android Studio](https://developer.android.com/studio)
2. Install with default settings
3. Open Android Studio
4. Go to **File > Open** and select the `vibejournal` folder
5. Wait for Gradle sync to complete
6. Android Studio will automatically handle all dependencies

---

## Option 2: Manual Setup (Advanced Users)

If you prefer to set up the development environment manually:

### Step 1: Install Java JDK
1. Download [Oracle JDK 11 or 17](https://www.oracle.com/java/technologies/downloads/)
2. Install it
3. Set `JAVA_HOME` environment variable:
   - **Windows**: 
     - Open System Properties > Environment Variables
     - Add `JAVA_HOME` pointing to JDK installation (e.g., `C:\Program Files\Java\jdk-17`)
     - Add `%JAVA_HOME%\bin` to your `PATH`
   - **Mac/Linux**: Add to `~/.bashrc` or `~/.zshrc`:
     ```bash
     export JAVA_HOME=/path/to/your/jdk
     export PATH=$JAVA_HOME/bin:$PATH
     ```

### Step 2: Install Android SDK
1. Download [Android SDK Command Line Tools](https://developer.android.com/studio#command-tools)
2. Extract and set `ANDROID_HOME` environment variable
3. Install required packages:
   ```bash
   sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
   ```

### Step 3: Build the Project
```bash
# Windows
.\gradlew.bat build

# Mac/Linux  
./gradlew build
```

---

## Current Issue Resolution

You're getting the error because Java is not installed or not in your PATH.

**Quick Fix - Install Android Studio:**
1. Download and install [Android Studio](https://developer.android.com/studio)
2. Android Studio will handle all the Java/Gradle setup automatically
3. Open the VibeJournal project in Android Studio
4. Let it sync and install any missing components

**Alternative - Manual Java Installation:**
1. Download [Java JDK](https://www.oracle.com/java/technologies/downloads/)
2. Install it
3. Restart your command prompt/terminal
4. Try building again

---

## After Setup

Once you have the prerequisites installed:

1. **Continue with Google Cloud Console setup** (see GOOGLE_SETUP_GUIDE.md)
2. **Set up OpenAI API key** (see README_MVP.md)
3. **Build and run the app**

## Recommended Workflow

For the best development experience:
1. **Install Android Studio** (handles everything automatically)
2. **Open the project** in Android Studio
3. **Use Android Studio's built-in terminal** for any command line operations
4. **Use the SHA-1 generation method** from Android Studio (Gradle signingReport task)

This approach eliminates most setup headaches and provides the best Android development experience.
