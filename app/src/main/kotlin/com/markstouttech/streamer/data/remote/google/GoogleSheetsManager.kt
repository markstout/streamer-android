package com.markstouttech.streamer.data.remote.google

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.google.api.services.sheets.v4.model.Sheet
import com.google.api.services.sheets.v4.model.SheetProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleSheetsManager(credential: GoogleAccountCredential) {

    private val transport = NetHttpTransport()
    private val jsonFactory = GsonFactory.getDefaultInstance()
    private val appName = "Movie TV Streamer"

    private val driveService: Drive = Drive.Builder(transport, jsonFactory, credential)
        .setApplicationName(appName)
        .build()

    private val sheetsService: Sheets = Sheets.Builder(transport, jsonFactory, credential)
        .setApplicationName(appName)
        .build()

    private val spreadsheetName = "Movie TV Streamer Data"

    suspend fun getOrCreateSpreadsheetId(): String = withContext(Dispatchers.IO) {
        val query = "name = '$spreadsheetName' and mimeType = 'application/vnd.google-apps.spreadsheet' and trashed = false"
        val files = driveService.files().list().setQ(query).execute().files
        
        if (files != null && files.isNotEmpty()) {
            return@withContext files[0].id
        } else {
            return@withContext createSpreadsheet()
        }
    }

    suspend fun fetchTitles(spreadsheetId: String): List<List<Any>> = withContext(Dispatchers.IO) {
        val response = sheetsService.spreadsheets().values()
            .get(spreadsheetId, "Titles!A2:M")
            .execute()
        return@withContext response.getValues() ?: emptyList()
    }

    suspend fun fetchEpisodes(spreadsheetId: String): List<List<Any>> = withContext(Dispatchers.IO) {
        val response = sheetsService.spreadsheets().values()
            .get(spreadsheetId, "Episodes!A2:H")
            .execute()
        return@withContext response.getValues() ?: emptyList()
    }

    private fun createSpreadsheet(): String {
        val spreadsheet = Spreadsheet().setProperties(
            SpreadsheetProperties().setTitle(spreadsheetName)
        ).setSheets(listOf(
            createSheet("User_Settings"),
            createSheet("Titles"),
            createSheet("Episodes")
        ))
        
        val created = sheetsService.spreadsheets().create(spreadsheet).execute()
        
        // Initialize headers
        initializeHeaders(created.spreadsheetId)
        
        return created.spreadsheetId
    }

    private fun createSheet(title: String): Sheet {
        return Sheet().setProperties(SheetProperties().setTitle(title))
    }

    private fun initializeHeaders(spreadsheetId: String) {
        updateValues(spreadsheetId, "User_Settings!A1", listOf(listOf("Key", "Value")))
        updateValues(spreadsheetId, "Titles!A1", listOf(listOf("ID", "Title", "Type", "Status", "Subscribed", "Poster_URL", "Rating", "Runtime", "Release_Date", "Description", "Genres", "IMDB_ID", "Last_Updated")))
        updateValues(spreadsheetId, "Episodes!A1", listOf(listOf("ID", "Title_ID", "Season", "Episode_Number", "Episode_Title", "Air_Date", "Watched", "Description")))
    }

    private fun updateValues(spreadsheetId: String, range: String, values: List<List<Any>>) {
        val body = com.google.api.services.sheets.v4.model.ValueRange().setValues(values)
        sheetsService.spreadsheets().values()
            .update(spreadsheetId, range, body)
            .setValueInputOption("RAW")
            .execute()
    }
}
