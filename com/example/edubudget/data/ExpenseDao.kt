package com.example.edubudget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expense")
    suspend fun getAllExpenses(): List<Expense>

    // ✅ ADD THIS (required for reset / new user)
    @Query("DELETE FROM expense")
    suspend fun deleteAll()
}