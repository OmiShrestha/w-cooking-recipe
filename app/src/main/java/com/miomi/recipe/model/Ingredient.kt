package com.miomi.recipe.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ingredient")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Int = 0,  // adding ingredientId as primary key for the Ingredient table
    val recipeId: Int,          // adding recipeId to link ingredient to its recipe
    val name: String,
    val quantity: Int,
    val unit: String
)
