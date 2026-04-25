package com.miomi.recipe.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ingredient")
data class Ingredient(
    @PrimaryKey
    val ingredientId: Int,
    val name: String,
    val quantity: Int,
    val unit: String
)