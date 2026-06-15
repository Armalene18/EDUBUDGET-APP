package com.example.edubudget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BadgeDao {

    @Insert
    suspend fun insert(badge: Badge)

    @Query("SELECT * FROM badge ORDER BY id DESC")
    suspend fun getAll(): List<Badge>

    @Query("SELECT COUNT(*) FROM badge WHERE title = :title")
    suspend fun countByTitle(title: String): Int
}
