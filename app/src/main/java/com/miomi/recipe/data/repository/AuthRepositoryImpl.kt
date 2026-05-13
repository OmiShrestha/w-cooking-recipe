package com.miomi.recipe.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.miomi.recipe.BuildConfig
import com.miomi.recipe.data.dao.UserDao
import com.miomi.recipe.model.User
import com.miomi.recipe.model.UserRole

// Repository class for handling user authentication and data storage
class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {

    //WEB_CLIENT_ID is the Oauth client id from google cloud that identifies app to google's sign in service

    private val webClientId = BuildConfig.WEB_CLIENT_ID
    private val adminEmails = setOf(BuildConfig.ADMIN_EMAIL_1, BuildConfig.ADMIN_EMAIL_2)

    override suspend fun signIn(activityContext: Context): Result<User> {
        //TODO: move this logic to VM instead of repo
        val credentialManager = CredentialManager.create(activityContext)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(activityContext, request)
            val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val email = credential.id
            val name = credential.displayName ?: email
            val profilePictureUrl = credential.profilePictureUri?.toString()
            val role = if (email in adminEmails) UserRole.ADMIN else UserRole.USER
            val user =
                User(email = email, name = name, profilePictureUrl = profilePictureUrl, role = role)
            userDao.insertUser(user)
            Result.success(user)
        } catch (e: GetCredentialException) {
            Result.failure(e)
        }
    }

    override suspend fun getLoggedInUser(): User? = userDao.getLoggedInUser()

    override suspend fun signOut() = userDao.clearUser()
}