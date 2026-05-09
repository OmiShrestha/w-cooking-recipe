package com.miomi.recipe.network.model

import com.google.gson.annotations.SerializedName

data class MealListResponse(
    @SerializedName("meals") val meals: List<MealDto>?
)
