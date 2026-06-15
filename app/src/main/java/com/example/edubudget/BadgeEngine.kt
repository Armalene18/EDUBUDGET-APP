package com.example.edubudget

import android.content.Context
import android.widget.Toast
import com.example.edubudget.data.AppDatabase
import com.example.edubudget.data.Badge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

object BadgeEngine {

    fun checkAndAwardBadges(context: Context) {
        val db = AppDatabase.getDatabase(context)
        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        CoroutineScope(Dispatchers.IO).launch {
            val expenses = db.expenseDao().getAllExpenses()
            val goals = db.budgetGoalDao().getAll()
            val totalSpent = expenses.sumOf { it.amount }
            val latestGoal = goals.lastOrNull()

            // Badge 1: First Expense Logged
            if (expenses.isNotEmpty() && db.badgeDao().countByTitle("First Step") == 0) {
                awardBadge(context, db, "🎯", "First Step", "Logged your very first expense!", today)
            }

            // Badge 2: Budget Master — total spent within max goal
            if (latestGoal != null && totalSpent <= latestGoal.maxAmount &&
                db.badgeDao().countByTitle("Budget Master") == 0
            ) {
                awardBadge(context, db, "🏆", "Budget Master", "Stayed within your maximum budget goal!", today)
            }

            // Badge 3: Saver — total spent at or below min goal
            if (latestGoal != null && totalSpent <= latestGoal.minAmount &&
                db.badgeDao().countByTitle("Saver") == 0
            ) {
                awardBadge(context, db, "💰", "Saver", "Spent at or below your minimum budget goal!", today)
            }

            // Badge 4: 5 Expenses logged
            if (expenses.size >= 5 && db.badgeDao().countByTitle("5 Expense Streak") == 0) {
                awardBadge(context, db, "🔥", "5 Expense Streak", "Logged 5 or more expenses!", today)
            }

            // Badge 5: 10 Expenses logged
            if (expenses.size >= 10 && db.badgeDao().countByTitle("10 Expense Streak") == 0) {
                awardBadge(context, db, "⚡", "10 Expense Streak", "Logged 10 or more expenses!", today)
            }
        }
    }

    private suspend fun awardBadge(
        context: Context,
        db: AppDatabase,
        emoji: String,
        title: String,
        description: String,
        date: String
    ) {
        db.badgeDao().insert(Badge(emoji = emoji, title = title, description = description, earnedDate = date))
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "$emoji Badge Earned: $title!", Toast.LENGTH_LONG).show()
        }
    }
}
