package com.miomi.recipe.data.repository

import android.content.Context
import com.miomi.recipe.model.User

interface AuthRepository {
    suspend fun signIn(activityContext: Context): Result<User>
    suspend fun getLoggedInUser(): User?
    suspend fun signOut()
}