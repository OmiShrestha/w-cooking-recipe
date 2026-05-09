package com.miomi.recipe.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit client singleton to provide MealApiService instance
object RetrofitClient {

    // Base URL for TheMealDB API
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    // OkHttpClient with logging interceptor for debugging
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        )
        .build()

    val mealApiService: MealApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiService::class.java)
    }
}
