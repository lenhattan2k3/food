package com.example.apporder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailField = findViewById<EditText>(R.id.usernameEditText)
        val passwordField = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton) // Nút đăng ký

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        checkUserRole()
                    } else {
                        Toast.makeText(
                            this,
                            "Đăng nhập thất bại: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // Khi bấm nút Đăng ký, chuyển hướng đến RegisterActivity
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun checkUserRole() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val database = FirebaseDatabase.getInstance().getReference("users").child(userId)
        database.get().addOnSuccessListener { snapshot ->
            val role = snapshot.child("role").value.toString()

            if (role == "admin") {
                // Nếu là admin, chuyển hướng đến AdminActivity
                startActivity(Intent(this, AdminActivity::class.java))
            } else {
                // Nếu là user, chuyển hướng đến UserActivity
                startActivity(Intent(this, UserActivity::class.java))
            }
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Lỗi khi kiểm tra vai trò", Toast.LENGTH_SHORT).show()
        }
    }
}
