package com.example.edubudget

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val createProfileBtn = findViewById<Button>(R.id.createProfileBtn)
        val toggleBtn = findViewById<Button>(R.id.togglePasswordBtn)

        toggleBtn.setOnClickListener {
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                passwordInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
                toggleBtn.text = "🙈"
            } else {
                passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
                toggleBtn.text = "👁"
            }
            passwordInput.setSelection(passwordInput.text.length)
        }

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
            val savedEmail = prefs.getString("email", "")
            val savedPassword = prefs.getString("password", "")
            if (email == savedEmail && password == savedPassword) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        createProfileBtn.setOnClickListener {
            startActivity(Intent(this, CreateProfileActivity::class.java))
        }
    }
}
