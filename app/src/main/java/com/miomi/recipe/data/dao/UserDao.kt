package com.miomi.recipe.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miomi.recipe.model.User

// Data Access Object for user-related database operations
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getLoggedInUser(): User?

    @Query("DELETE FROM user")
    suspend fun clearUser()
}
