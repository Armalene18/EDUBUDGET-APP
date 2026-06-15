package com.example.edubudget

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.edubudget.data.AppDatabase
import com.example.edubudget.data.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class ExpenseActivity : AppCompatActivity() {

    private var imageUri: Uri? = null

    private lateinit var desc: EditText
    private lateinit var date: EditText
    private lateinit var start: EditText
    private lateinit var end: EditText
    private lateinit var amount: EditText
    private lateinit var spinner: Spinner
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        db = AppDatabase.getDatabase(this)

        // Use IDs that match activity_expense.xml
        desc = findViewById(R.id.descriptionInput)
        date = findViewById(R.id.dateInput)
        start = findViewById(R.id.startTimeInput)
        end = findViewById(R.id.endTimeInput)
        amount = findViewById(R.id.amountInput)
        spinner = findViewById(R.id.categorySpinner)

        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)

        val pickBtn = findViewById<Button?>(R.id.pickImageBtn)
        val saveBtn = findViewById<Button>(R.id.saveExpenseBtn)

        date.setOnClickListener { showDatePicker() }
        start.setOnClickListener { showTimePicker(start) }
        end.setOnClickListener { showTimePicker(end) }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            imageView.setImageURI(uri)
        }

        pickBtn?.setOnClickListener { pickImage.launch("image/*") }
        saveBtn.setOnClickListener { saveExpense() }
    }

    override fun onResume() {
        super.onResume()
        loadCategories()
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            date.setText("$day/${month + 1}/$year")
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePicker(target: EditText) {
        val cal = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            target.setText(String.format("%02d:%02d", hour, minute))
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun loadCategories() {
        lifecycleScope.launch(Dispatchers.IO) {
            val categories = db.categoryDao().getAllCategories()
            val names = categories.map { it.name }
            withContext(Dispatchers.Main) {
                val finalList = if (names.isEmpty()) listOf("No categories available") else names
                val adapter = ArrayAdapter(this@ExpenseActivity, android.R.layout.simple_spinner_item, finalList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
    }

    private fun saveExpense() {
        val descText = desc.text.toString().trim()
        val dateText = date.text.toString().trim()
        val startText = start.text.toString().trim()
        val endText = end.text.toString().trim()
        val amountText = amount.text.toString().trim()
        val category = spinner.selectedItem?.toString()

        if (descText.isEmpty() || dateText.isEmpty() || amountText.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            return
        }
        if (category.isNullOrEmpty() || category == "No categories available") {
            Toast.makeText(this, "Please add a category first", Toast.LENGTH_SHORT).show()
            return
        }
        val amountValue = amountText.toDoubleOrNull()
        if (amountValue == null) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val expense = Expense(
            description = descText, date = dateText, startTime = startText,
            endTime = endText, category = category, amount = amountValue,
            imageUri = imageUri?.toString()
        )

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { progressBar.visibility = View.VISIBLE }
            db.expenseDao().insert(expense)
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@ExpenseActivity, "Expense saved", Toast.LENGTH_SHORT).show()
                clearFields()
            }
        }
    }

    private fun clearFields() {
        desc.text.clear(); date.text.clear(); start.text.clear()
        end.text.clear(); amount.text.clear()
        imageView.setImageDrawable(null); imageUri = null
    }
}
