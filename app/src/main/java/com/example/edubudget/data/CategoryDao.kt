package com.example.edubudget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {

    @Insert
    suspend fun insert(category: Category)

    @Query("SELECT * FROM category")
    suspend fun getAllCategories(): List<Category>

    // ✅ ADD THIS (required for reset / new user)
    @Query("DELETE FROM category")
    suspend fun deleteAll()
}