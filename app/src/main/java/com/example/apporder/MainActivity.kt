package com.example.apporder

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        inputEditText = findViewById(R.id.btn2)
        addButton = findViewById(R.id.btn1)

        // Set up button click listener
        addButton.setOnClickListener {
            val inputData = inputEditText.text.toString().trim()

            if (inputData.isNotEmpty()) {
                // Add data to Firebase
                addDataToFirebase(inputData)
            } else {
                // Show a message if the input is empty
                Toast.makeText(this, "Please enter some data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addDataToFirebase(data: String) {
        // Get a reference to the Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().reference

        // Create a new child node in Firebase with the input data
        val newDataRef = database.child("data").push()
        newDataRef.setValue(data)
            .addOnSuccessListener {
                // Show success message
                Toast.makeText(this, "Data added successfully!", Toast.LENGTH_SHORT).show()

                // Clear the input field
                inputEditText.text.clear()
            }
            .addOnFailureListener { exception ->
                // Show error message
                Toast.makeText(this, "Failed to add data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
