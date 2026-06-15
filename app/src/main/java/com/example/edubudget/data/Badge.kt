package com.example.edubudget.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badge")
data class Badge(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val emoji: String,
    val title: String,
    val description: String,
    val earnedDate: String
)
