package com.example.apporder

import android.net.Uri
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FirebaseHelper {

    // Function to upload the dish with an image
    fun addDishWithImage(dish: Dish, imageUri: Uri, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Create a storage reference with a unique filename
        val storageReference = FirebaseStorage.getInstance().reference.child("dish_images/${System.currentTimeMillis()}.jpg")

        // Upload the image to Firebase Storage
        storageReference.putFile(imageUri)
            .addOnSuccessListener { _ -> // Renamed taskSnapshot to _
                // Get the image URL after upload success
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    Log.d("FirebaseHelper", "Image uploaded successfully, URL: $imageUrl")

                    // Create a new Dish object with the image URL
                    val dishWithImageUrl = dish.copy(imageUrl = imageUrl)

                    // Save dish data (including image URL) in Firebase Realtime Database
                    val databaseReference = FirebaseDatabase.getInstance().getReference("dishes")
                    databaseReference.push().setValue(dishWithImageUrl)
                        .addOnSuccessListener {
                            Log.d("FirebaseHelper", "Dish added successfully to database")
                            onSuccess()  // Call success callback
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirebaseHelper", "Error saving dish data to database: ${e.message}")
                            onFailure(e.message ?: "Error saving dish data to database")  // Call failure callback with error message
                        }
                }
                    .addOnFailureListener { e ->
                        Log.e("FirebaseHelper", "Error retrieving image URL after upload: ${e.message}")
                        onFailure(e.message ?: "Error retrieving image URL after upload")  // Call failure callback if image URL fetch fails
                    }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseHelper", "Image upload failed: ${e.message}")
                onFailure(e.message ?: "Image upload failed")  // Call failure callback if image upload fails
            }
    }

    // Function to load all dishes from the Firebase Database
    fun getDishes(onSuccess: (List<Dish>) -> Unit, onFailure: (String) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("dishes")
        databaseReference.get()
            .addOnSuccessListener { snapshot ->
                val dishes = mutableListOf<Dish>()
                for (childSnapshot in snapshot.children) {
                    val dish = childSnapshot.getValue(Dish::class.java)
                    dish?.let { dishes.add(it) }
                }
                Log.d("FirebaseHelper", "Dishes fetched successfully from database")
                onSuccess(dishes)  // Call success callback with dishes list
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseHelper", "Failed to load dishes: ${e.message}")
                onFailure(e.message ?: "Failed to load dishes")  // Call failure callback if fetching dishes fails
            }
    }
}
