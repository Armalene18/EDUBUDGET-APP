package com.example.edubudget

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.edubudget.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpenseListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)
        title = "All Expenses"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val listView = findViewById<ListView>(R.id.expenseListView)
        val db = AppDatabase.getDatabase(this)

        val textColor     = if (isDarkMode()) 0xFFEFEFEF.toInt() else 0xFF1A1A1A.toInt()
        val subTextColor  = if (isDarkMode()) 0xFFBBBBBB.toInt() else 0xFF666666.toInt()
        val cardBgColor   = if (isDarkMode()) 0xFF1E1E1E.toInt() else 0xFFFFFFFF.toInt()
        val screenBgColor = if (isDarkMode()) 0xFF121212.toInt() else 0xFFE8F5F0.toInt()
        val dividerColor  = if (isDarkMode()) 0xFF333333.toInt() else 0xFFE0E0E0.toInt()

        listView.setBackgroundColor(screenBgColor)

        lifecycleScope.launch(Dispatchers.IO) {
            val expenses = db.expenseDao().getAllExpenses()
            withContext(Dispatchers.Main) {
                if (expenses.isEmpty()) {
                    // Show empty state as a single non-invisible row
                    val emptyAdapter = object : ArrayAdapter<String>(
                        this@ExpenseListActivity,
                        android.R.layout.simple_list_item_1,
                        listOf("No expenses recorded yet.")
                    ) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val tv = TextView(context)
                            tv.text = getItem(position)
                            tv.textSize = 15f
                            tv.setTextColor(subTextColor)
                            tv.setBackgroundColor(screenBgColor)
                            tv.setPadding(32, 32, 32, 32)
                            return tv
                        }
                    }
                    listView.adapter = emptyAdapter
                    return@withContext
                }

                val adapter = object : ArrayAdapter<com.example.edubudget.data.Expense>(
                    this@ExpenseListActivity,
                    0,
                    expenses
                ) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val expense = getItem(position)!!

                        val card = LinearLayout(context).apply {
                            orientation = LinearLayout.VERTICAL
                            setBackgroundColor(cardBgColor)
                            setPadding(32, 20, 32, 20)
                        }

                        // Top row: description + amount
                        val topRow = LinearLayout(context).apply {
                            orientation = LinearLayout.HORIZONTAL
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }

                        val descTv = TextView(context).apply {
                            text = "💸  ${expense.description}"
                            textSize = 15f
                            setTextColor(textColor)
                            setTypeface(null, android.graphics.Typeface.BOLD)
                            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        }

                        val amountTv = TextView(context).apply {
                            text = "R${expense.amount}"
                            textSize = 15f
                            setTextColor(0xFFEF4444.toInt())
                            setTypeface(null, android.graphics.Typeface.BOLD)
                        }

                        topRow.addView(descTv)
                        topRow.addView(amountTv)

                        // Category + date row
                        val metaTv = TextView(context).apply {
                            text = "${expense.category}  •  ${expense.date}"
                            textSize = 12f
                            setTextColor(subTextColor)
                            setPadding(0, 6, 0, 0)
                        }

                        card.addView(topRow)
                        card.addView(metaTv)
                        return card
                    }
                }

                listView.adapter = adapter
                listView.divider = android.graphics.drawable.ColorDrawable(dividerColor)
                listView.dividerHeight = 2
                listView.setOnItemClickListener { _, _, position, _ ->
                    val e = expenses[position]
                    Toast.makeText(
                        this@ExpenseListActivity,
                        "${e.description} — R${e.amount}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun isDarkMode(): Boolean {
        return (resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
