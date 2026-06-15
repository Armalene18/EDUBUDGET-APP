package com.example.edubudget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SavingsGoalDao {

    @Insert
    suspend fun insert(goal: SavingsGoal)

    @Update
    suspend fun update(goal: SavingsGoal)

    @Query("SELECT * FROM savings_goal")
    suspend fun getAll(): List<SavingsGoal>

    @Query("DELETE FROM savings_goal WHERE id = :id")
    suspend fun deleteById(id: Int)
}
