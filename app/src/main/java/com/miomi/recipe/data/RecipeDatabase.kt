package com.miomi.recipe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.miomi.recipe.data.dao.IngredientDao
import com.miomi.recipe.data.dao.RecipeDao
import com.miomi.recipe.data.dao.StepDao
import com.miomi.recipe.model.Ingredient
import com.miomi.recipe.model.Recipe
import com.miomi.recipe.model.Step
import com.miomi.recipe.data.dao.UserDao
import com.miomi.recipe.model.User

// Room database class for the recipe app
@Database(entities = [Recipe::class, Ingredient::class, Step::class, User::class], version = 2,
    exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun stepDao(): StepDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: RecipeDatabase? = null
        fun getDatabase(context: Context): RecipeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context,
                    RecipeDatabase::class.java,
                    "recipe_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}