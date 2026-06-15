package com.example.edubudget

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.edubudget.data.AppDatabase
import com.example.edubudget.data.SavingsGoal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavingsGoalActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings_goal)

        title = "Savings Goals"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase.getDatabase(this)

        val goalNameInput = findViewById<EditText>(R.id.goalNameInput)
        val targetAmountInput = findViewById<EditText>(R.id.targetAmountInput)
        val savedAmountInput = findViewById<EditText>(R.id.savedAmountInput)
        val saveBtn = findViewById<Button>(R.id.saveSavingsGoalBtn)
        val goalsContainer = findViewById<LinearLayout>(R.id.goalsContainer)

        saveBtn.setOnClickListener {
            val name = goalNameInput.text.toString().trim()
            val target = targetAmountInput.text.toString().toDoubleOrNull()
            val saved = savedAmountInput.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isEmpty() || target == null) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                db.savingsGoalDao().insert(SavingsGoal(name = name, targetAmount = target, savedAmount = saved))
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SavingsGoalActivity, "Savings goal saved!", Toast.LENGTH_SHORT).show()
                    goalNameInput.text.clear()
                    targetAmountInput.text.clear()
                    savedAmountInput.text.clear()
                    loadGoals(goalsContainer)
                }
            }
        }

        loadGoals(goalsContainer)
    }

    private fun isDarkMode(): Boolean {
        return (resources.configuration.uiMode and
            android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
            android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    private fun loadGoals(container: LinearLayout) {
        lifecycleScope.launch(Dispatchers.IO) {
            val goals = db.savingsGoalDao().getAll()
            withContext(Dispatchers.Main) {
                // Explicit colors — no attr resolution that can silently fail
                val textColor = if (isDarkMode()) 0xFFEFEFEF.toInt() else 0xFF1A1A1A.toInt()
                val subTextColor = if (isDarkMode()) 0xFFBBBBBB.toInt() else 0xFF666666.toInt()
                val cardBgColor = if (isDarkMode()) 0xFF1E1E1E.toInt() else 0xFFFFFFFF.toInt()
                val dividerColor = if (isDarkMode()) 0xFF333333.toInt() else 0xFFE0E0E0.toInt()

                container.removeAllViews()

                if (goals.isEmpty()) {
                    val tv = TextView(this@SavingsGoalActivity)
                    tv.text = "No savings goals yet. Add one above!"
                    tv.textSize = 15f
                    tv.setTextColor(subTextColor)
                    tv.setPadding(32, 32, 32, 32)
                    container.addView(tv)
                } else {
                    val titleText = TextView(this@SavingsGoalActivity)
                    titleText.text = "🎯 Your Savings Goals"
                    titleText.textSize = 16f
                    titleText.setTextColor(textColor)
                    titleText.setTypeface(null, android.graphics.Typeface.BOLD)
                    titleText.setPadding(32, 32, 32, 16)
                    container.addView(titleText)

                    goals.forEach { goal ->
                        val percent = if (goal.targetAmount > 0)
                            ((goal.savedAmount / goal.targetAmount) * 100).toInt().coerceAtMost(100)
                        else 0

                        val card = LinearLayout(this@SavingsGoalActivity).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).also { it.bottomMargin = 4 }
                            orientation = LinearLayout.VERTICAL
                            setBackgroundColor(cardBgColor)
                            setPadding(32, 24, 32, 20)
                        }

                        val nameText = TextView(this@SavingsGoalActivity).apply {
                            text = "🎯 ${goal.name}"
                            textSize = 16f
                            setTextColor(textColor)
                            setTypeface(null, android.graphics.Typeface.BOLD)
                            setPadding(0, 0, 0, 6)
                        }

                        val detailText = TextView(this@SavingsGoalActivity).apply {
                            text = "Target: R%.2f  •  Saved: R%.2f  •  Progress: %d%%"
                                .format(goal.targetAmount, goal.savedAmount, percent)
                            textSize = 13f
                            setTextColor(subTextColor)
                            setPadding(0, 0, 0, 10)
                        }

                        val progressBar = ProgressBar(
                            this@SavingsGoalActivity, null,
                            android.R.attr.progressBarStyleHorizontal
                        ).apply {
                            max = 100
                            progress = percent
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                16
                            )
                        }

                        card.addView(nameText)
                        card.addView(detailText)
                        card.addView(progressBar)
                        container.addView(card)

                        val divider = android.view.View(this@SavingsGoalActivity).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, 1
                            )
                            setBackgroundColor(dividerColor)
                        }
                        container.addView(divider)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
