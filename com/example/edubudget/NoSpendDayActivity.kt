package com.example.edubudget

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.edubudget.data.AppDatabase
import com.example.edubudget.data.Badge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class NoSpendDayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_spend_day)

        title = "No Spend Day Challenge"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val statusText = findViewById<TextView>(R.id.noSpendStatusText)
        val checkBtn = findViewById<Button>(R.id.checkNoSpendBtn)
        val db = AppDatabase.getDatabase(this)

        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        checkBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val expenses = db.expenseDao().getAllExpenses()
                val todayExpenses = expenses.filter { it.date == today }

                withContext(Dispatchers.Main) {
                    if (todayExpenses.isEmpty()) {
                        statusText.text = "🎉 Amazing! You haven't spent anything today!\nYou completed the No Spend Day Challenge!"
                        statusText.setTextColor(android.graphics.Color.parseColor("#4CAF50"))

                        // Award badge if not already earned today
                        lifecycleScope.launch(Dispatchers.IO) {
                            val alreadyEarned = db.badgeDao().countByTitle("No Spend Hero") > 0
                            if (!alreadyEarned) {
                                db.badgeDao().insert(
                                    Badge(
                                        emoji = "⭐",
                                        title = "No Spend Hero",
                                        description = "Completed a No Spend Day challenge!",
                                        earnedDate = today
                                    )
                                )
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@NoSpendDayActivity,
                                        "⭐ Badge Earned: No Spend Hero!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } else {
                        statusText.text = "💸 You've spent R%.2f today across ${todayExpenses.size} expense(s).\nTry again tomorrow!"
                            .format(todayExpenses.sumOf { it.amount })
                        statusText.setTextColor(android.graphics.Color.parseColor("#F44336"))
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
