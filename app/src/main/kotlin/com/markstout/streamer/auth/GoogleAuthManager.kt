package com.markstout.streamer.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.drive.DriveScopes
import com.google.api.services.sheets.v4.SheetsScopes

class GoogleAuthManager(private val context: Context) {

    private val scopes = listOf(
        DriveScopes.DRIVE_FILE,
        SheetsScopes.SPREADSHEETS
    )

    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(com.google.android.gms.common.api.Scope(DriveScopes.DRIVE_FILE))
            .requestScopes(com.google.android.gms.common.api.Scope(SheetsScopes.SPREADSHEETS))
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    fun getCredential(account: GoogleSignInAccount): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(context, scopes)
            .setBackOff(ExponentialBackOff())
            .setSelectedAccount(account.account)
    }

    fun getLastSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}
