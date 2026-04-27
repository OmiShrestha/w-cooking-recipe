package com.miomi.recipe.model

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeWithDetails (
    @Embedded val recipe: Recipe,
    @Relation (
        parentColumn = "recipeId",
        entityColumn = "recipeId"
    )
    val ingredients: List<Ingredient>,
    @Relation (
        parentColumn = "recipeId",
        entityColumn = "recipeId"
    )
    val steps: List<Step>
)