package com.miomi.recipe

import android.app.Application
import com.miomi.recipe.data.repository.AuthRepository
import com.miomi.recipe.data.RecipeDatabase
import com.miomi.recipe.data.repository.AuthRepositoryImpl
import com.miomi.recipe.data.repository.RecipeRepository
import com.miomi.recipe.data.repository.RecipeRepositoryImpl
import com.miomi.recipe.network.RetrofitClient
import com.miomi.recipe.network.repository.MealRepository
import com.miomi.recipe.network.repository.MealRepositoryImpl

class RecipeApplication : Application() {
    lateinit var recipeRepository: RecipeRepository
    lateinit var mealRepository: MealRepository
    lateinit var authRepository: AuthRepository

    override fun onCreate() {
        super.onCreate()
        val database = RecipeDatabase.getDatabase(this)
        recipeRepository = RecipeRepositoryImpl(database)
        mealRepository = MealRepositoryImpl(RetrofitClient.mealApiService)
        authRepository = AuthRepositoryImpl(this, database.userDao())
    }
}