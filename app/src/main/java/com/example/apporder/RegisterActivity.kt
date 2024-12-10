package com.example.apporder

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailField = findViewById<EditText>(R.id.emailEditText)
        val passwordField = findViewById<EditText>(R.id.passwordEditText)
        val roleField = findViewById<EditText>(R.id.roleEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val role = roleField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: ""
                        val database = FirebaseDatabase.getInstance().getReference("users").child(userId)

                        val user = mapOf(
                            "email" to email,
                            "role" to role
                        )

                        database.setValue(user).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this, "Lỗi: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Đăng ký thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
