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
import com.khangle.myfitnessadmin.model.BodyStat


class BodyStatAdapter(val onItemclick: (item: BodyStat) -> Unit):
    ListAdapter<BodyStat, BodyStatAdapter.BodyStatVH>(nutritionCategoryDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyStatVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_body_stat, parent, false)
        return BodyStatVH(view, onItemclick)
    }

    override fun onBindViewHolder(holder: BodyStatVH, position: Int) {
        holder.bind(getItem(position))
    }

    class BodyStatVH(itemView: View, val onItemclick: (item: BodyStat) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val dataTypeTextView: TextView
        val nameTextView: TextView
        lateinit var item: BodyStat
        init {
            nameTextView = itemView.findViewById(R.id.nameTextView)
            dataTypeTextView = itemView.findViewById(R.id.dataTypeTextview)
            itemView.setOnClickListener {
                onItemclick(item)
            }
        }
        fun bind(bodyStat: BodyStat) {
            item = bodyStat
            nameTextView.text = bodyStat.name
            dataTypeTextView.text = bodyStat.dataType
        }
    }
}
val nutritionCategoryDiffUtil = object : DiffUtil.ItemCallback<BodyStat>() {
    override fun areItemsTheSame(oldItem: BodyStat, newItem: BodyStat): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(
        oldItem: BodyStat,
        newItem: BodyStat
    ): Boolean {
        return (oldItem.name == newItem.name && oldItem.dataType == newItem.dataType)
    }
}
