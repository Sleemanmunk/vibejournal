# Google Cloud Console Setup Guide for VibeJournal

This guide will walk you through setting up Google Cloud Console credentials for the VibeJournal app.

## Step 1: Create a Google Cloud Project

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Sign in with your Google account
3. Click on the project dropdown at the top of the page
4. Click "New Project"
5. Enter project details:
   - **Project name**: `vibejournal-app` (or your preferred name)
   - **Organization**: Leave as default or select your organization
   - **Location**: Leave as default
6. Click "Create"
7. Wait for the project to be created and select it

## Step 2: Enable Required APIs

1. In the Google Cloud Console, go to **APIs & Services** > **Library**
2. Search for and enable the following APIs:

### Enable Google Docs API:
   - Search for "Google Docs API"
   - Click on "Google Docs API"
   - Click "Enable"

### Enable Google Drive API:
   - Search for "Google Drive API" 
   - Click on "Google Drive API"
   - Click "Enable"

## Step 3: Create OAuth 2.0 Credentials

1. Go to **APIs & Services** > **Credentials**
2. Click **"+ CREATE CREDENTIALS"** at the top
3. Select **"OAuth client ID"**

### Configure OAuth Consent Screen (if not done already):
If you see a message about configuring the OAuth consent screen:
1. Click "Configure Consent Screen"
2. Select **"External"** (unless you have a Google Workspace account)
3. Click "Create"
4. Fill in the required fields:
   - **App name**: `VibeJournal`
   - **User support email**: Your email
   - **Developer contact information**: Your email
5. Click "Save and Continue"
6. Skip the "Scopes" section for now (click "Save and Continue")
7. Add test users (your email address)
8. Click "Save and Continue"
9. Review and click "Back to Dashboard"

### Create Android Credentials:
1. Back in **Credentials**, click **"+ CREATE CREDENTIALS"** > **"OAuth client ID"**
2. Select **"Android"** as application type
3. Fill in the details:
   - **Name**: `VibeJournal Android Client`
   - **Package name**: `com.vibejournal.app`
   - **SHA-1 certificate fingerprint**: (we'll get this next)

## Step 4: Get SHA-1 Certificate Fingerprint

### Method 1: Using Android Studio (Recommended)

**Quick Method - Use Terminal in Android Studio:**
1. **Open Android Studio and load the VibeJournal project**
2. **Open Terminal**: Go to **View > Tool Windows > Terminal**
3. **Run the command**: Type `gradlew signingReport` and press Enter
4. **Find the SHA1**: Look for the debug variant output

**Detailed Method - Use Gradle Tab:**
1. **First, open the project in Android Studio:**
   - Open Android Studio
   - Click "Open an existing project"
   - Navigate to and select the `vibejournal` folder
   - Wait for Gradle sync to complete

2. **Generate SHA-1 using Gradle (Multiple Methods):**

   **Method A: Gradle Tab (Right Side)**
   - Look for **Gradle** tab on the right side of Android Studio
   - If you don't see it, go to **View > Tool Windows > Gradle**
   - Expand your project name (e.g., **vibejournal**)
   - Expand **app** > **Tasks** > **android**
   - Double-click on **signingReport**
   - Check the **Run** tab at the bottom for output

   **Method B: Terminal in Android Studio**
   - Go to **View > Tool Windows > Terminal**
   - In the terminal, type: `./gradlew signingReport` (Mac/Linux) or `gradlew signingReport` (Windows)
   - Press Enter and wait for the output

   **Method C: Build Menu**
   - Go to **Build > Generate Signed Bundle / APK**
   - This will show signing information in the process

   **Method D: Project Structure**
   - Go to **File > Project Structure**
   - Select **Modules** > **app** > **Signing**
   - You can see or create signing configs here

   **Look for output like:**
   ```
   Variant: debug
   Config: debug
   Store: /Users/your-name/.android/debug.keystore
   Alias: AndroidDebugKey
   MD5: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX
   SHA1: AA:BB:CC:DD:EE:FF:00:11:22:33:44:55:66:77:88:99:AA:BB:CC:DD
   SHA-256: ...
   ```
   
   **Copy the SHA1 line** (the one that starts with `SHA1:`)

### Method 2: Using Command Line (if keytool is available)

**On Windows (PowerShell):**
```powershell
# First check if debug keystore exists
Test-Path "$env:USERPROFILE\.android\debug.keystore"

# If it exists, run:
keytool -list -v -keystore "$env:USERPROFILE\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```

**On Mac/Linux:**
```bash
# First check if debug keystore exists
ls ~/.android/debug.keystore

# If it exists, run:
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

### Method 3: If Debug Keystore Doesn't Exist

If you get "keystore doesn't exist" errors, you need to create it first:

1. **Run any Android project once** to generate the debug keystore automatically
2. **Or create it manually** (advanced):
   ```powershell
   # On Windows
   keytool -genkey -v -keystore "$env:USERPROFILE\.android\debug.keystore" -storepass android -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Android Debug,O=Android,C=US"
   ```

### Expected Output:
Look for a line like:
```
SHA1: AA:BB:CC:DD:EE:FF:00:11:22:33:44:55:66:77:88:99:AA:BB:CC:DD
```

**Copy this entire SHA-1 value** (including the colons) - you'll need it for the Google Cloud Console.

## Step 5: Complete OAuth Client Setup

1. Paste the SHA-1 fingerprint into the **"SHA-1 certificate fingerprint"** field
2. Click **"Create"**
3. You'll see a confirmation dialog with your client details
4. Click **"OK"**

## Step 6: Download google-services.json

1. Go to **Project Settings** (gear icon) > **General**
2. Scroll down to **"Your apps"** section
3. Click **"Add app"** or the Android icon
4. Enter package name: `com.vibejournal.app`
5. Enter app nickname: `VibeJournal`
6. Add the same SHA-1 certificate fingerprint
7. Click **"Register app"**
8. Download the **`google-services.json`** file
9. Click **"Continue"** and **"Finish"**

## Step 7: Add google-services.json to Your Project

1. Take the downloaded `google-services.json` file
2. Replace the placeholder file in your project:
   - Delete: `vibejournal/app/google-services.json`
   - Copy your downloaded file to: `vibejournal/app/google-services.json`

## Step 8: Verify Setup

Your `google-services.json` should look something like this (with your actual values):

```json
{
  "project_info": {
    "project_number": "123456789012",
    "project_id": "vibejournal-app-xxxxx"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:123456789012:android:abcdef123456",
        "android_client_info": {
          "package_name": "com.vibejournal.app"
        }
      },
      "oauth_client": [
        {
          "client_id": "123456789012-abcdefghijklmnop.apps.googleusercontent.com",
          "client_type": 1,
          "android_info": {
            "package_name": "com.vibejournal.app",
            "certificate_hash": "your_sha1_hash_here"
          }
        }
      ]
    }
  ]
}
```

## Testing Your Setup

1. Open Android Studio
2. Build and run the app
3. Tap "Sign in with Google"
4. You should see the Google sign-in flow
5. After signing in, you should be able to access the main app features

## Common Issues and Solutions

### Issue: "Can't find signingReport in Gradle tab"
- **Solution 1**: Make sure Gradle tab is visible: **View > Tool Windows > Gradle**
- **Solution 2**: Try refreshing Gradle: Click the refresh icon in Gradle tab
- **Solution 3**: Use Terminal method: **View > Tool Windows > Terminal**, then type `gradlew signingReport`
- **Solution 4**: If Gradle sync failed, try: **File > Sync Project with Gradle Files**
- **Solution 5**: Sometimes it's under different paths:
  - Try: **app > Tasks > other** (instead of android)
  - Or: **root project > Tasks > android > signingReport**

### Issue: "Gradle sync failed"
- **Solution**: Install Java Development Kit (JDK) or use Android Studio method
- **Download JDK**: https://www.oracle.com/java/technologies/downloads/
- **Or use Method 1** (Android Studio) which is easier and doesn't require separate Java installation

### Issue: "Keystore does not exist"
- **Solution**: The debug keystore is created automatically when you first run an Android project
- **Quick fix**: Open Android Studio, create a new Android project, and run it once. This will generate the debug keystore
- **Then** come back to this project and try getting the SHA-1 again

### Issue: "OAuth client not found"
- **Solution**: Make sure the package name matches exactly: `com.vibejournal.app`
- Check that SHA-1 fingerprint is correct

### Issue: "Sign-in failed"
- **Solution**: Verify the `google-services.json` file is in the correct location (`app/` folder)
- Make sure you've enabled both Google Docs API and Google Drive API

### Issue: "API not enabled"
- **Solution**: Go back to APIs & Services > Library and enable the required APIs

### Issue: "App not verified"
- **Solution**: For development, you can continue with the warning. For production, you'll need to verify your app with Google.

## Production Notes

For production release:
1. Generate a release SHA-1 fingerprint using your release keystore
2. Add the release SHA-1 to your OAuth client credentials
3. Submit your app for OAuth verification if needed
4. Consider creating separate OAuth clients for debug and release builds

## Next Steps

Once your Google Cloud Console setup is complete:
1. Set up your OpenAI API key (see main README)
2. Build and test the app
3. You should be able to sign in and access Google Docs!
