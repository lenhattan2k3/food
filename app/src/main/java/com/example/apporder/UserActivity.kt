package com.example.apporder

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.Toast
import android.widget.ViewSwitcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class UserActivity : AppCompatActivity() {

    // Category RecyclerView
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryList = mutableListOf(
        Category("Pizza", R.drawable.cat_1),
        Category("Hamburger", R.drawable.cat_2),
        Category("HotDog", R.drawable.cat_3),
        Category("Water", R.drawable.cat_4),
        Category("Sushi", R.drawable.cat_5) // New category added
    )

    // Food RecyclerView
    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodAdapter: DishAdapter
    private val foodList = mutableListOf<Dish>()

    // ImageSwitcher for promotional images
    private lateinit var imageSwitcher: ImageSwitcher
    private val imageResources = listOf(
        R.drawable.ic_qc1, // Replace with actual image resources
        R.drawable.ic_qc2,
        R.drawable.ic_qc3
    )
    private var currentImageIndex = 0
    private val switchInterval: Long = 3000 // 3 seconds

    // Marquee TextView
    private lateinit var marqueeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Initialize Category RecyclerView
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(categoryList)
        categoryRecyclerView.adapter = categoryAdapter

        // Initialize Food RecyclerView
        foodRecyclerView = findViewById(R.id.foodRecyclerView)
        foodRecyclerView.layoutManager = LinearLayoutManager(this)
        foodAdapter = DishAdapter(foodList)
        foodRecyclerView.adapter = foodAdapter

        // Initialize ImageSwitcher
        imageSwitcher = findViewById(R.id.imageSwitcher)
        imageSwitcher.setFactory(object : ViewSwitcher.ViewFactory {
            override fun makeView(): ImageView {
                val imageView = ImageView(this@UserActivity)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.layoutParams = android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT
                )
                return imageView
            }
        })

        // Set initial image for ImageSwitcher
        imageSwitcher.setImageResource(imageResources[currentImageIndex])

        // Start image switching effect
        startImageSwitching()

        // Initialize Marquee TextView
        marqueeTextView = findViewById(R.id.marqueeTextView)
        marqueeTextView.isSelected = true // This ensures the marquee effect starts

        // Load food items from Firebase
        loadFoodItems()
    }

    // Function to start image switching every 3 seconds
    private fun startImageSwitching() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                currentImageIndex = (currentImageIndex + 1) % imageResources.size
                imageSwitcher.setImageResource(imageResources[currentImageIndex])
                handler.postDelayed(this, switchInterval)
            }
        }
        handler.post(runnable)
    }

    // Function to load food items from Firebase
    private fun loadFoodItems() {
        FirebaseDatabase.getInstance().getReference("dishes")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    foodList.clear()
                    snapshot.children.forEach {
                        val dish = it.getValue(Dish::class.java)
                        dish?.let { foodList.add(it) }
                    }
                    foodAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@UserActivity, "Error loading dishes", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
