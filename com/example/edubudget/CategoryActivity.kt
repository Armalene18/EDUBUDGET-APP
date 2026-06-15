package com.example.edubudget

import android.os.Bundle
import android.util.TypedValue
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.edubudget.data.AppDatabase
import com.example.edubudget.data.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var categoriesContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        title = "Categories"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase.getDatabase(this)

        val categoryInput = findViewById<EditText>(R.id.categoryInput)
        val saveBtn = findViewById<Button>(R.id.saveCategoryBtn)
        categoriesContainer = findViewById(R.id.categoriesContainer)

        saveBtn.setOnClickListener {
            val categoryName = categoryInput.text.toString().trim()
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO) {
                db.categoryDao().insert(Category(name = categoryName))
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CategoryActivity, "Category added!", Toast.LENGTH_SHORT).show()
                    categoryInput.text.clear()
                    loadCategories()
                }
            }
        }

        loadCategories()
    }

    private fun isDarkMode(): Boolean {
        return (resources.configuration.uiMode and
            android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
            android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    private fun loadCategories() {
        lifecycleScope.launch(Dispatchers.IO) {
            val categories = db.categoryDao().getAllCategories()
            withContext(Dispatchers.Main) {
                // Explicit dark/light colors — no attr resolution that can silently fail
                val textColor = if (isDarkMode()) 0xFFEFEFEF.toInt() else 0xFF1A1A1A.toInt()
                val subTextColor = if (isDarkMode()) 0xFFBBBBBB.toInt() else 0xFF666666.toInt()
                val cardBgColor = if (isDarkMode()) 0xFF1E1E1E.toInt() else 0xFFFFFFFF.toInt()
                val dividerColor = if (isDarkMode()) 0xFF333333.toInt() else 0xFFE0E0E0.toInt()
                val screenBgColor = if (isDarkMode()) 0xFF121212.toInt() else 0xFFE8F5F0.toInt()

                categoriesContainer.setBackgroundColor(screenBgColor)
                categoriesContainer.removeAllViews()

                if (categories.isEmpty()) {
                    val emptyText = TextView(this@CategoryActivity)
                    emptyText.text = "No categories yet. Add one above!"
                    emptyText.textSize = 15f
                    emptyText.setTextColor(subTextColor)
                    emptyText.setPadding(32, 32, 32, 32)
                    categoriesContainer.addView(emptyText)
                } else {
                    val titleText = TextView(this@CategoryActivity)
                    titleText.text = "📂 Existing Categories"
                    titleText.textSize = 16f
                    titleText.setTextColor(textColor)
                    titleText.setTypeface(null, android.graphics.Typeface.BOLD)
                    titleText.setPadding(32, 32, 32, 16)
                    categoriesContainer.addView(titleText)

                    categories.forEach { category ->
                        val rowLayout = LinearLayout(this@CategoryActivity).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            orientation = LinearLayout.HORIZONTAL
                            setBackgroundColor(cardBgColor)
                            setPadding(32, 28, 32, 28)
                        }

                        val categoryText = TextView(this@CategoryActivity).apply {
                            text = "🏷️  ${category.name}"
                            textSize = 15f
                            setTextColor(textColor)
                            layoutParams = LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                            )
                        }

                        rowLayout.addView(categoryText)
                        categoriesContainer.addView(rowLayout)

                        val divider = android.view.View(this@CategoryActivity).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, 1
                            )
                            setBackgroundColor(dividerColor)
                        }
                        categoriesContainer.addView(divider)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
