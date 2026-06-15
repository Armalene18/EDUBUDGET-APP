package com.example.edubudget.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense")
data class Expense(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val description: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val category: String,

    val amount: Double,

    val imageUri: String?
)