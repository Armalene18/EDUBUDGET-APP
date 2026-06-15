package com.example.edubudget

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.edubudget.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        db = AppDatabase.getDatabase(this)
        DatabaseSeeder.seed(this)
        NotificationHelper.createNotificationChannel(this)
        NotificationHelper.scheduleDailyReminder(this)

        // Welcome by first name
        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val firstName = prefs.getString("firstName", null)
            ?: prefs.getString("fullName", "")?.split(" ")?.firstOrNull()
            ?: "Student"
        findViewById<TextView>(R.id.welcomeText).text = "Welcome, $firstName!"

        // Dark mode switch — fully functional
        val darkModeSwitch = findViewById<Switch>(R.id.darkModeSwitch)
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        darkModeSwitch.isChecked = currentMode == AppCompatDelegate.MODE_NIGHT_YES
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Quick action buttons
        findViewById<Button>(R.id.btnExpense).setOnClickListener {
            startActivity(Intent(this, ExpenseActivity::class.java))
        }
        findViewById<Button>(R.id.btnCategory).setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
        findViewById<Button?>(R.id.btnGoal)?.setOnClickListener {
            startActivity(Intent(this, BudgetGoalActivity::class.java))
        }
        findViewById<Button?>(R.id.btnViewExpenses)?.setOnClickListener {
            startActivity(Intent(this, ExpenseListActivity::class.java))
        }
        findViewById<Button>(R.id.btnGraph).setOnClickListener {
            startActivity(Intent(this, SpendingGraphActivity::class.java))
        }
        findViewById<Button>(R.id.btnBadges).setOnClickListener {
            startActivity(Intent(this, BadgesActivity::class.java))
        }
        findViewById<Button>(R.id.btnSavings).setOnClickListener {
            startActivity(Intent(this, SavingsGoalActivity::class.java))
        }
        findViewById<Button?>(R.id.btnNoSpend)?.setOnClickListener {
            startActivity(Intent(this, NoSpendDayActivity::class.java))
        }

        // Bottom Navigation Tabs
        findViewById<Button>(R.id.tabDashboard).setOnClickListener {
            // Already on dashboard — scroll to top or do nothing
        }
        findViewById<Button>(R.id.tabExpense).setOnClickListener {
            startActivity(Intent(this, ExpenseActivity::class.java))
        }
        findViewById<Button>(R.id.tabCategories).setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
        findViewById<Button>(R.id.tabReports).setOnClickListener {
            startActivity(Intent(this, SpendingGraphActivity::class.java))
        }
        findViewById<Button>(R.id.tabProfile).setOnClickListener {
            startActivity(Intent(this, CreateProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadDashboard()
        BadgeEngine.checkAndAwardBadges(this)
    }

    private fun loadDashboard() {
        lifecycleScope.launch(Dispatchers.IO) {
            val expenses = db.expenseDao().getAllExpenses()
            val goals = db.budgetGoalDao().getAll()
            val total = expenses.sumOf { it.amount }
            val count = expenses.size
            val latestGoal = goals.lastOrNull()
            val minGoal = latestGoal?.minAmount ?: 0.0
            val maxGoal = latestGoal?.maxAmount ?: 0.0
            val safeToSpend = (maxGoal - total).coerceAtLeast(0.0)
            val progressPercent = if (maxGoal > 0) ((total / maxGoal) * 100).toInt().coerceAtMost(100) else 0
            val statusMsg = when {
                maxGoal == 0.0 -> "Set a budget goal to track progress"
                total > maxGoal -> "⚠️ Over budget! Spent R%.2f over limit".format(total - maxGoal)
                total > minGoal -> "✅ On track! Within your budget range"
                else -> "💚 Great! Spending below minimum goal"
            }
            withContext(Dispatchers.Main) {
                findViewById<TextView>(R.id.totalExpensesText).text = "Total Spent: R%.2f".format(total)
                findViewById<TextView?>(R.id.expenseCountText)?.text = "Expenses Logged: $count"
                findViewById<TextView>(R.id.safeToSpendText).text = "Safe to Spend: R%.2f".format(safeToSpend)
                findViewById<TextView>(R.id.goalRangeText).text = "Budget Goal: R%.2f – R%.2f".format(minGoal, maxGoal)
                findViewById<ProgressBar>(R.id.budgetProgressBar).progress = progressPercent
                val statusView = findViewById<TextView?>(R.id.budgetStatusText)
                statusView?.text = statusMsg
                statusView?.setTextColor(
                    if (total > maxGoal && maxGoal > 0)
                        android.graphics.Color.parseColor("#F44336")
                    else android.graphics.Color.parseColor("#4CAF50")
                )
            }
        }
    }
}
