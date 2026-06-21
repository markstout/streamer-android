# Linking to Google Drive and Sheets

To enable synchronization between the Android app and Google Drive, follow these steps:

## 1. Google Cloud Project Setup
1.  Go to the [Google Cloud Console](https://console.cloud.google.com/).
2.  Create a new project named "Movie TV Streamer".
3.  Enable the following APIs:
    *   **Google Drive API**
    *   **Google Sheets API**

## 2. Configure OAuth Consent Screen
1.  Navigate to "APIs & Services" > "OAuth consent screen".
2.  Choose "External" user type.
3.  Fill in the app information (App name: Movie TV Streamer).
4.  Add the following scopes:
    *   `https://www.googleapis.com/auth/drive.file` (Access to files created or opened by the app)
    *   `https://www.googleapis.com/auth/spreadsheets` (Read/write access to spreadsheets)

## 3. Create Credentials
1.  Navigate to "APIs & Services" > "Credentials".
2.  Click "Create Credentials" > "OAuth client ID".
3.  Select "Android" as the application type.
4.  Enter your package name: `com.markstout.streamer`.
5.  Enter your SHA-1 certificate fingerprint:
    *   You can get this by running `./gradlew signingReport` in Android Studio.
6.  Click "Create".

## 4. App-Side Integration
The app is configured to automatically:
1.  **Search:** Look for a spreadsheet named `Movie TV Streamer Data` in the user's Google Drive.
2.  **Create:** If not found, it creates the spreadsheet with the required sheets:
    *   `User_Settings` (Columns: `Key`, `Value`)
    *   `Titles` (Columns: `ID`, `Title`, `Type`, `Status`, `Subscribed`, `Poster_URL`, `Rating`, `Runtime`, `Release_Date`, `Description`, `Genres`, `IMDB_ID`, `Last_Updated`)
    *   `Episodes` (Columns: `ID`, `Title_ID`, `Season`, `Episode_Number`, `Episode_Title`, `Air_Date`, `Watched`, `Description`)
3.  **Sync:** Perform bi-directional synchronization using `last_updated` timestamps to resolve conflicts.

## 5. First Run
When you first launch the app and trigger a sync:
1.  The app will prompt you to sign in with your Google Account.
2.  It will request permission to access your Google Drive files.
3.  Once granted, it will initialize the spreadsheet.
