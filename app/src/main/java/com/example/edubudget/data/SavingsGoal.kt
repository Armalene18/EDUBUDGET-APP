package com.example.edubudget.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goal")
data class SavingsGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double = 0.0
)
