package com.khangle.myfitnessadmin.excercise.category

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
import com.khangle.myfitnessadmin.model.ExcerciseCategory

class ExcCategoryListAdapter(val onItemclick: (item: ExcerciseCategory) -> Unit):
    ListAdapter<ExcerciseCategory, ExcCategoryListAdapter.ExcerciseCategoryVH>(excerciseCategoryDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExcerciseCategoryVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ExcerciseCategoryVH(view, onItemclick)
    }

    override fun onBindViewHolder(holder: ExcerciseCategoryVH, position: Int) {
        holder.bind(getItem(position))
    }

    class ExcerciseCategoryVH(itemView: View, val onItemclick: (item: ExcerciseCategory) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView
        val textView: TextView
        lateinit var item: ExcerciseCategory
        init {
            imageView = itemView.findViewById(R.id.categoryIcon)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            textView = itemView.findViewById(R.id.categoryNameTextview)
            itemView.findViewById<CardView>(R.id.cardItem).setOnClickListener {
                onItemclick(item)
            }
        }
        fun bind(excerciseCategory: ExcerciseCategory) {
            item = excerciseCategory
            imageView.load(excerciseCategory.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
            }
            textView.text = excerciseCategory.name
        }
    }
}
val excerciseCategoryDiffUtil = object : DiffUtil.ItemCallback<ExcerciseCategory>() {
    override fun areItemsTheSame(oldItem: ExcerciseCategory, newItem: ExcerciseCategory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ExcerciseCategory,
        newItem: ExcerciseCategory
    ): Boolean {
        return (oldItem.name == newItem.name && oldItem.photoUrl == newItem.photoUrl)
    }

}

