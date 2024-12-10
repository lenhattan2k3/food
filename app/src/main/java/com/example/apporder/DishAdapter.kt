package com.example.apporder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DishAdapter(private val dishes: List<Dish>) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        holder.bind(dish)
    }

    override fun getItemCount() = dishes.size

    class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.dishName)
        private val priceText: TextView = itemView.findViewById(R.id.dishPrice)
        private val infoText: TextView = itemView.findViewById(R.id.dishInfo)
        private val imageView: ImageView = itemView.findViewById(R.id.dishImage)

        fun bind(dish: Dish) {
            nameText.text = dish.name
            priceText.text = "Price: ${dish.price}"
            infoText.text = dish.info
            Glide.with(itemView.context).load(dish.imageUrl).into(imageView)
        }
    }
}
