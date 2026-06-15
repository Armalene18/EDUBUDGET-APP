package com.example.edubudget

import android.widget.EditText

object ValidationUtils {

    fun isNotEmpty(field: EditText, errorMessage: String): Boolean {
        val value = field.text.toString().trim()

        return if (value.isEmpty()) {
            field.error = errorMessage
            field.requestFocus()
            false
        } else {
            true
        }
    }

    fun validateAll(vararg fields: Pair<EditText, String>): Boolean {
        var valid = true

        for (field in fields) {
            val editText = field.first
            val message = field.second

            if (editText.text.toString().trim().isEmpty()) {
                editText.error = message
                editText.requestFocus()
                valid = false
            }
        }

        return valid
    }
}