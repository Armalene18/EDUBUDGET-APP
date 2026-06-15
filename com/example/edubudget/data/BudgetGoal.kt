package com.example.edubudget.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_goal")
data class BudgetGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val minAmount: Double,
    val maxAmount: Double
)