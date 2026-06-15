package com.example.edubudget

import android.content.Context
import com.example.edubudget.data.AppDatabase
import com.example.edubudget.data.Category
import com.example.edubudget.data.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseSeeder {

    fun seed(context: Context) {
        val db = AppDatabase.getDatabase(context)

        CoroutineScope(Dispatchers.IO).launch {

            if (db.categoryDao().getAllCategories().isNotEmpty()) return@launch

            // SA Student-focused categories
            val categories = listOf(
                Category(name = "NSFAS/Allowance"),
                Category(name = "Rent/Res"),
                Category(name = "Taxi Fare"),
                Category(name = "Data Bundles"),
                Category(name = "Groceries"),
                Category(name = "Entertainment"),
                Category(name = "Stationery"),
                Category(name = "Medical")
            )

            categories.forEach { db.categoryDao().insert(it) }

            // Sample expenses
            val expenses = listOf(
                Expense(
                    description = "Monthly Res Fee",
                    date = "01/06/2026",
                    startTime = "09:00",
                    endTime = "09:10",
                    category = "Rent/Res",
                    amount = 2500.0,
                    imageUri = null
                ),
                Expense(
                    description = "Taxi to campus",
                    date = "02/06/2026",
                    startTime = "07:30",
                    endTime = "08:00",
                    category = "Taxi Fare",
                    amount = 30.0,
                    imageUri = null
                ),
                Expense(
                    description = "Data bundle",
                    date = "03/06/2026",
                    startTime = "10:00",
                    endTime = "10:05",
                    category = "Data Bundles",
                    amount = 149.0,
                    imageUri = null
                ),
                Expense(
                    description = "Lunch",
                    date = "04/06/2026",
                    startTime = "12:00",
                    endTime = "12:30",
                    category = "Groceries",
                    amount = 75.0,
                    imageUri = null
                )
            )

            expenses.forEach { db.expenseDao().insert(it) }
        }
    }
}
