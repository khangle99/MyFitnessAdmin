package com.khangle.myfitnessadmin.nutrition.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.model.NutritionCategory


class NutCategoryListAdapter(val onItemclick: (item: NutritionCategory) -> Unit):
    ListAdapter<NutritionCategory, NutCategoryListAdapter.NutritionCategoryVH>(nutritionCategoryDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionCategoryVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return NutritionCategoryVH(view, onItemclick)
    }

    override fun onBindViewHolder(holder: NutritionCategoryVH, position: Int) {
        holder.bind(getItem(position))
    }

    class NutritionCategoryVH(itemView: View, val onItemclick: (item: NutritionCategory) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView
        val textView: TextView
        lateinit var item: NutritionCategory
        init {
            imageView = itemView.findViewById(R.id.categoryIcon)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            textView = itemView.findViewById(R.id.categoryNameTextview)
            itemView.findViewById<CardView>(R.id.cardItem).setOnClickListener {
                onItemclick(item)
            }
        }
        fun bind(nutritionCategory: NutritionCategory) {
            item = nutritionCategory
            imageView.load(nutritionCategory.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
            }
            textView.text = nutritionCategory.name
        }
    }
}
val nutritionCategoryDiffUtil = object : DiffUtil.ItemCallback<NutritionCategory>() {
    override fun areItemsTheSame(oldItem: NutritionCategory, newItem: NutritionCategory): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(
        oldItem: NutritionCategory,
        newItem: NutritionCategory
    ): Boolean {
        return (oldItem.name == newItem.name && oldItem.photoUrl == newItem.photoUrl)
    }
}
