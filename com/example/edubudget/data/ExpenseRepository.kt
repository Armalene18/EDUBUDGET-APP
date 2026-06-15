package com.example.edubudget.data

class ExpenseRepository(private val db: AppDatabase) {

    suspend fun getExpenses() = db.expenseDao().getAllExpenses()

    suspend fun insertExpense(expense: Expense) {
        db.expenseDao().insert(expense)
    }

    suspend fun getCategories() = db.categoryDao().getAllCategories()
}