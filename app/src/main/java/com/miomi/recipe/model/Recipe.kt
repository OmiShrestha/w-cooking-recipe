package com.miomi.recipe.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey (autoGenerate = true) val recipeId: Int = 0,
    val name: String,
    val category: String,
    val isFavorite: Boolean
)
