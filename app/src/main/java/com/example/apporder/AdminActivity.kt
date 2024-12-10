package com.example.apporder

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.util.Log

class AdminActivity : AppCompatActivity() {

    private lateinit var dishNameField: EditText
    private lateinit var dishPriceField: EditText
    private lateinit var dishAddressField: EditText
    private lateinit var dishInfoField: EditText
    private lateinit var addDishButton: Button
    private lateinit var dishListRecyclerView: RecyclerView
    private lateinit var dishAdapter: DishAdapter

    private val dishList = mutableListOf<Dish>()
    private val defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/user-a28cc.appspot.com/o/bobittet.png?alt=media&token=7d72f3ac-d842-4243-83a4-a62a163ffcc6"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        dishNameField = findViewById(R.id.dishNameField)
        dishPriceField = findViewById(R.id.dishPriceField)
        dishAddressField = findViewById(R.id.dishAddressField)
        dishInfoField = findViewById(R.id.dishInfoField)
        addDishButton = findViewById(R.id.addDishButton)
        dishListRecyclerView = findViewById(R.id.dishListRecyclerView)

        // Set up RecyclerView with adapter
        dishListRecyclerView.layoutManager = LinearLayoutManager(this)
        dishAdapter = DishAdapter(dishList)
        dishListRecyclerView.adapter = dishAdapter

        // Load existing dishes from Firebase
        loadDishes()

        // Add dish when button clicked
        addDishButton.setOnClickListener {
            val name = dishNameField.text.toString().trim()
            val price = dishPriceField.text.toString().trim()
            val address = dishAddressField.text.toString().trim()
            val info = dishInfoField.text.toString().trim()

            // Validate input fields
            if (name.isNotEmpty() && price.isNotEmpty() && address.isNotEmpty() && info.isNotEmpty()) {
                saveDishToDatabase(name, price, address, info, defaultImageUrl)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDishToDatabase(name: String, price: String, address: String, info: String, imageUrl: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val database = FirebaseDatabase.getInstance().getReference("dishes")
        val key = database.push().key ?: return
        val dish = Dish(name, price, address, info, imageUrl, currentUser.uid)

        // Save dish to Firebase
        database.child(key).setValue(dish).addOnSuccessListener {
            Toast.makeText(this, "Dish added successfully", Toast.LENGTH_SHORT).show()
            // Add new dish to local list and notify adapter
            dishList.add(dish)
            dishAdapter.notifyItemInserted(dishList.size - 1)
            clearFields() // Clear input fields after adding
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to add dish", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadDishes() {
        // Listen for changes in the 'dishes' node in Firebase
        val database = FirebaseDatabase.getInstance().getReference("dishes")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dishList.clear() // Clear previous data
                snapshot.children.forEach {
                    val dish = it.getValue(Dish::class.java)
                    dish?.let { dishList.add(it) } // Add dish to the list if it's not null
                }
                dishAdapter.notifyDataSetChanged() // Notify the adapter of data changes
            }

            override fun onCancelled(error: DatabaseError) {
                // Log and show error message
                Log.e("AdminActivity", "Error Code: ${error.code}, Details: ${error.details}, Message: ${error.message}")
                Toast.makeText(this@AdminActivity, "Error loading dishes: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun clearFields() {
        // Clear input fields after adding the dish
        dishNameField.text.clear()
        dishPriceField.text.clear()
        dishAddressField.text.clear()
        dishInfoField.text.clear()
    }
}
