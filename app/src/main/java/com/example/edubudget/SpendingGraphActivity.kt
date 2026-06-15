package com.example.edubudget

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.edubudget.data.AppDatabase
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpendingGraphActivity : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_graph)

        title = "Spending Graph"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        barChart = findViewById(R.id.barChart)
        db = AppDatabase.getDatabase(this)

        loadGraph()
    }

    private fun loadGraph() {
        lifecycleScope.launch(Dispatchers.IO) {

            val expenses = db.expenseDao().getAllExpenses()
            val goals = db.budgetGoalDao().getAll()

            val categoryTotals = expenses
                .groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }

            val categories = categoryTotals.keys.toList()
            val totals = categoryTotals.values.toList()

            val minGoal = goals.lastOrNull()?.minAmount?.toFloat() ?: 0f
            val maxGoal = goals.lastOrNull()?.maxAmount?.toFloat() ?: 0f

            withContext(Dispatchers.Main) {

                if (categories.isEmpty()) {
                    Toast.makeText(this@SpendingGraphActivity, "No expenses to display", Toast.LENGTH_SHORT).show()
                    return@withContext
                }

                val entries = totals.mapIndexed { index, value ->
                    BarEntry(index.toFloat(), value.toFloat())
                }

                val dataSet = BarDataSet(entries, "Spending per Category")
                dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
                dataSet.valueTextSize = 12f
                dataSet.valueTextColor = android.graphics.Color.parseColor("#1a1a1a")

                val barData = BarData(dataSet)
                barChart.data = barData

                // X axis configuration for full label visibility
                barChart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(categories)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    textSize = 12f
                    labelRotationAngle = 0f // Keep horizontal
                    setAvoidFirstLastClipping(false)
                }

                // Y axis
                barChart.axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = android.graphics.Color.parseColor("#e0e0e0")
                    textSize = 11f
                }

                // Min goal line (green)
                if (minGoal > 0) {
                    val minLine = LimitLine(minGoal, "Min Goal: R${"%.0f".format(minGoal)}")
                    minLine.lineColor = android.graphics.Color.parseColor("#4CAF50")
                    minLine.lineWidth = 3f
                    minLine.textColor = android.graphics.Color.parseColor("#4CAF50")
                    minLine.textSize = 12f
                    barChart.axisLeft.addLimitLine(minLine)
                }

                // Max goal line (red)
                if (maxGoal > 0) {
                    val maxLine = LimitLine(maxGoal, "Max Goal: R${"%.0f".format(maxGoal)}")
                    maxLine.lineColor = android.graphics.Color.parseColor("#F44336")
                    maxLine.lineWidth = 3f
                    maxLine.textColor = android.graphics.Color.parseColor("#F44336")
                    maxLine.textSize = 12f
                    barChart.axisLeft.addLimitLine(maxLine)
                }

                barChart.axisRight.isEnabled = false
                barChart.description.isEnabled = false
                barChart.legend.isEnabled = true
                barChart.setFitBars(true)
                barChart.setScaleEnabled(true)
                barChart.setPinchZoom(true)
                barChart.animateY(1000)
                barChart.invalidate()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
