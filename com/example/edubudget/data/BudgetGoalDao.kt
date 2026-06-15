package com.example.edubudget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BudgetGoalDao {

    @Insert
    suspend fun insert(goal: BudgetGoal)

    @Query("SELECT * FROM budget_goal")
    suspend fun getAll(): List<BudgetGoal>
}