package com.miomi.recipe.network.model

import com.google.gson.annotations.SerializedName

// Data class representing the response from TheMealDB API when searching for meals
data class MealListResponse(
    @SerializedName("meals") val meals: List<MealDto>?
)
