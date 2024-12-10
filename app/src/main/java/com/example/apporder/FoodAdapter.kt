package com.example.apporder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apporder.databinding.ItemFoodBinding

class FoodAdapter(private val foodList: List<FoodItem>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodList[position]
        holder.bind(foodItem)
    }

    override fun getItemCount(): Int = foodList.size

    class FoodViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(foodItem: FoodItem) {
            binding.tvFoodName.text = foodItem.name
            binding.tvFoodPrice.text = foodItem.price
        }
    }
}
