package com.ceph.pulsepoint.presentation.authentication

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.ceph.pulsepoint.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthClient(
    private val context: Context,
) {


    private val auth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(context)
    private val webClient = context.getString(R.string.web_client)

    suspend fun signInWithCredentialManager(): SignInResult {
        return try {
            val googleOption = GetGoogleIdOption.Builder()
                .setServerClientId(webClient)
                .setAutoSelectEnabled(false)
                .setFilterByAuthorizedAccounts(false)
                .build()
            val passwordOption = GetPasswordOption()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleOption)
                .addCredentialOption(passwordOption)
                .build()

            val credentialResponse = credentialManager.getCredential(context, request)
            val credential = credentialResponse.credential
            authenticateWithFirebase(credential)

        } catch (e: NoCredentialException) {
            Log.d("TAG", "signInWithCredentialManager: ${e.message}")
            SignInResult(null, "No credential found")
        } catch (e: GetCredentialException) {
            Log.d("TAG", "signInWithCredentialManager: ${e.message}")
            SignInResult(null, "No credential found")
        }
    }

    private suspend fun authenticateWithFirebase(credential: Credential): SignInResult {

        return try {
            val googleCredentialOption = GoogleIdTokenCredential.createFrom(credential.data)
            val googleTokenId = googleCredentialOption.idToken

            if (googleTokenId.isEmpty()) {
                SignInResult(null, " No google idToken")
            }

            val authCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
            val user = auth.signInWithCredential(authCredential).await().user

            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        photoUrl = photoUrl.toString(),
                        email = email

                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(null, e.message)
        }
    }


    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        username: String
    ): SignInResult {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = username,
                        photoUrl = photoUrl.toString(),
                        email = email
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }

    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        photoUrl = photoUrl.toString(),
                        email = email
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }

    }


    fun getCurrentUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            userName = displayName,
            photoUrl = photoUrl.toString(),
            email = email
        )
    }

    suspend fun signOut() {
        try {
            val request = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(request)
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}