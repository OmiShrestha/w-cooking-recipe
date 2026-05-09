package com.miomi.recipe.network

import com.miomi.recipe.network.model.MealListResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit API service interface for TheMealDB
interface MealApiService {

    // GET request to search meals by name
    @GET("search.php")
    suspend fun searchByName(@Query("s") name: String): MealListResponse

    // GET request to filter meals by category
    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") category: String): MealListResponse

    // GET request to filter meals by ingredient
    @GET("filter.php")
    suspend fun filterByIngredient(@Query("i") ingredient: String): MealListResponse

    // GET request to lookup meal details by ID
    @GET("lookup.php")
    suspend fun lookupById(@Query("i") id: String): MealListResponse
}
