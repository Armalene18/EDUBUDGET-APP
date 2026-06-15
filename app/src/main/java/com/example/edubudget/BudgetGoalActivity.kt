package com.example.edubudget

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.edubudget.data.AppDatabase
import com.example.edubudget.data.BudgetGoal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class BudgetGoalActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val format = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_goal)
        title = "Budget Goal"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase.getDatabase(this)

        val minSeek = findViewById<SeekBar>(R.id.minSeek)
        val maxSeek = findViewById<SeekBar>(R.id.maxSeek)
        val minText = findViewById<TextView>(R.id.minText)
        val maxText = findViewById<TextView>(R.id.maxText)
        val minInput = findViewById<EditText>(R.id.minInput)
        val maxInput = findViewById<EditText>(R.id.maxInput)
        val saveBtn = findViewById<Button>(R.id.saveGoalBtn)
        val goalsContainer = findViewById<LinearLayout>(R.id.existingGoalsContainer)

        var minValue = 0.0
        var maxValue = 0.0

        // SeekBar → update text and input field
        minSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    minValue = progress.toDouble()
                    minText.text = "Min: ${format.format(minValue)}"
                    minInput.setText(progress.toString())
                }
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        maxSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    maxValue = progress.toDouble()
                    maxText.text = "Max: ${format.format(maxValue)}"
                    maxInput.setText(progress.toString())
                }
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        // Manual text entry → update seekbar and value
        minInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val typed = minInput.text.toString().toDoubleOrNull()
                if (typed != null) {
                    minValue = typed
                    minText.text = "Min: ${format.format(minValue)}"
                    minSeek.progress = typed.toInt().coerceAtMost(10000)
                }
            }
        }
        maxInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val typed = maxInput.text.toString().toDoubleOrNull()
                if (typed != null) {
                    maxValue = typed
                    maxText.text = "Max: ${format.format(maxValue)}"
                    maxSeek.progress = typed.toInt().coerceAtMost(10000)
                }
            }
        }

        saveBtn.setOnClickListener {
            // Prefer manual input over seekbar if filled in
            val typedMin = minInput.text.toString().toDoubleOrNull()
            val typedMax = maxInput.text.toString().toDoubleOrNull()
            if (typedMin != null) minValue = typedMin
            if (typedMax != null) maxValue = typedMax

            if (minValue <= 0 && maxValue <= 0) {
                Toast.makeText(this, "Please set a budget amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (minValue >= maxValue) {
                Toast.makeText(this, "Min must be less than Max", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveBtn.isEnabled = false
            lifecycleScope.launch(Dispatchers.IO) {
                db.budgetGoalDao().insert(BudgetGoal(minAmount = minValue, maxAmount = maxValue))
                withContext(Dispatchers.Main) {
                    saveBtn.isEnabled = true
                    Toast.makeText(this@BudgetGoalActivity, "Budget Goal Saved!", Toast.LENGTH_SHORT).show()
                    minInput.text.clear()
                    maxInput.text.clear()
                    minSeek.progress = 0
                    maxSeek.progress = 0
                    minText.text = "Min: R0.00"
                    maxText.text = "Max: R0.00"
                    loadExistingGoals(goalsContainer)
                }
            }
        }

        loadExistingGoals(goalsContainer)
    }

    private fun isDarkMode(): Boolean {
        return (resources.configuration.uiMode and
            android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
            android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    private fun loadExistingGoals(container: LinearLayout) {
        lifecycleScope.launch(Dispatchers.IO) {
            val goals = db.budgetGoalDao().getAll()
            withContext(Dispatchers.Main) {
                val textColor = if (isDarkMode()) 0xFFEFEFEF.toInt() else 0xFF1A1A1A.toInt()
                val subTextColor = if (isDarkMode()) 0xFFBBBBBB.toInt() else 0xFF666666.toInt()
                val cardBgColor = if (isDarkMode()) 0xFF1E1E1E.toInt() else 0xFFFFFFFF.toInt()
                val dividerColor = if (isDarkMode()) 0xFF333333.toInt() else 0xFFE0E0E0.toInt()

                container.removeAllViews()
                if (goals.isEmpty()) {
                    val tv = TextView(this@BudgetGoalActivity)
                    tv.text = "No goals saved yet."
                    tv.textSize = 14f
                    tv.setTextColor(subTextColor)
                    tv.setPadding(32, 24, 32, 24)
                    container.addView(tv)
                } else {
                    val titleTv = TextView(this@BudgetGoalActivity)
                    titleTv.text = "🎯 Existing Budget Goals"
                    titleTv.textSize = 16f
                    titleTv.setTextColor(textColor)
                    titleTv.setTypeface(null, android.graphics.Typeface.BOLD)
                    titleTv.setPadding(32, 24, 32, 16)
                    container.addView(titleTv)

                    goals.reversed().forEachIndexed { index, goal ->
                        val row = android.widget.LinearLayout(this@BudgetGoalActivity).apply {
                            layoutParams = android.widget.LinearLayout.LayoutParams(
                                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            orientation = android.widget.LinearLayout.VERTICAL
                            setBackgroundColor(cardBgColor)
                            setPadding(32, 20, 32, 20)
                        }
                        val tv = TextView(this@BudgetGoalActivity)
                        tv.text = "📌 Goal ${goals.size - index}: Min ${format.format(goal.minAmount)} – Max ${format.format(goal.maxAmount)}"
                        tv.textSize = 14f
                        tv.setTextColor(textColor)
                        row.addView(tv)
                        container.addView(row)

                        val divider = android.view.View(this@BudgetGoalActivity)
                        divider.layoutParams = android.widget.LinearLayout.LayoutParams(
                            android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1)
                        divider.setBackgroundColor(dividerColor)
                        container.addView(divider)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
