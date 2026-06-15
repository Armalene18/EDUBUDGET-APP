package com.example.edubudget

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CreateProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private var selectedImageUri: Uri? = null
    private val REQUEST_IMAGE_PICK = 100
    private var passwordVisible = false
    private var confirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        title = "Create Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fullNameInput = findViewById<EditText>(R.id.fullNameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirmPasswordInput)
        val createBtn = findViewById<Button>(R.id.createProfileBtn)
        val togglePasswordBtn = findViewById<Button>(R.id.togglePasswordBtn)
        val toggleConfirmBtn = findViewById<Button>(R.id.toggleConfirmPasswordBtn)
        val alreadyHaveAccount = findViewById<TextView>(R.id.alreadyHaveAccountText)
        profileImageView = findViewById(R.id.profileImageView)

        updateProfileImage("JD")

        profileImageView.setOnClickListener { showImagePickerDialog() }

        togglePasswordBtn.setOnClickListener {
            passwordVisible = !passwordVisible
            passwordInput.transformationMethod = if (passwordVisible)
                HideReturnsTransformationMethod.getInstance()
            else PasswordTransformationMethod.getInstance()
            togglePasswordBtn.text = if (passwordVisible) "🙈" else "👁"
            passwordInput.setSelection(passwordInput.text.length)
        }

        toggleConfirmBtn.setOnClickListener {
            confirmPasswordVisible = !confirmPasswordVisible
            confirmPasswordInput.transformationMethod = if (confirmPasswordVisible)
                HideReturnsTransformationMethod.getInstance()
            else PasswordTransformationMethod.getInstance()
            toggleConfirmBtn.text = if (confirmPasswordVisible) "🙈" else "👁"
            confirmPasswordInput.setSelection(confirmPasswordInput.text.length)
        }

        alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Update initials as name is typed
        fullNameInput.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name = s.toString().trim()
                if (name.isNotEmpty()) {
                    val initials = name.split(" ")
                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .take(2).joinToString("")
                    if (selectedImageUri == null) updateProfileImage(initials)
                } else {
                    if (selectedImageUri == null) updateProfileImage("JD")
                }
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        createBtn.setOnClickListener {
            val fullName = fullNameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val initials = fullName.split(" ")
                .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                .take(2).joinToString("")

            val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
            prefs.edit().apply {
                putString("fullName", fullName)
                putString("firstName", fullName.split(" ").firstOrNull() ?: fullName)
                putString("email", email)
                putString("password", password)
                putString("initials", initials)
                if (selectedImageUri != null) putString("profileImageUri", selectedImageUri.toString())
                apply()
            }

            Toast.makeText(this, "Profile created successfully!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

    private fun showImagePickerDialog() {
        AlertDialog.Builder(this)
            .setTitle("Change Profile Picture")
            .setItems(arrayOf("Choose from Gallery", "Cancel")) { _, which ->
                if (which == 0) pickImageFromGallery()
            }.show()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == REQUEST_IMAGE_PICK) {
            selectedImageUri = data.data
            profileImageView.setImageURI(selectedImageUri)
        }
    }

    fun updateProfileImage(initials: String) {
        val size = 200
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply { color = Color.parseColor("#2E7D32"); isAntiAlias = true }
        canvas.drawCircle((size / 2).toFloat(), (size / 2).toFloat(), (size / 2).toFloat(), paint)
        val textPaint = Paint().apply {
            color = Color.WHITE; textSize = 80f; isAntiAlias = true; textAlign = Paint.Align.CENTER
        }
        val yPos = (size / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)
        canvas.drawText(initials, (size / 2).toFloat(), yPos, textPaint)
        profileImageView.setImageBitmap(bitmap)
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
