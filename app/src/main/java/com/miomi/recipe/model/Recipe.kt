package com.miomi.recipe.model

data class Recipe(
    val id: Int,
    val name: String,
    val category: String,
    val ingredients: String,
    val instructions: String
)
