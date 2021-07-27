package com.khangle.myfitnessadmin.excercise.exclist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.model.Excercise


class ExcerciseListAdapter(val onItemclick: (item: Excercise) -> Unit):
    ListAdapter<Excercise, ExcerciseListAdapter.ExcerciseVH>(excerciseDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExcerciseVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_excercise, parent, false)
        return ExcerciseVH(view, onItemclick)
    }

    override fun onBindViewHolder(holder: ExcerciseVH, position: Int) {
        holder.bind(getItem(position))
    }

    class ExcerciseVH(itemView: View, val onItemclick: (item: Excercise) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView
        val difficultyTv: TextView
        val equipmentTv: TextView
        lateinit var item: Excercise
        init {
            nameTv = itemView.findViewById(R.id.excerciseNameTv)
            difficultyTv = itemView.findViewById(R.id.excDifficultyTv)
            equipmentTv = itemView.findViewById(R.id.excEquipmentTv)
            itemView.findViewById<CardView>(R.id.cardItem).setOnClickListener {
                onItemclick(item)
            }
        }
        fun bind(excercise: Excercise) {
            item = excercise
            nameTv.text = excercise.name
            difficultyTv.text = excercise.difficulty
            equipmentTv.text = excercise.equipment
        }
    }
}
val excerciseDiffUtil = object : DiffUtil.ItemCallback<Excercise>() {
    override fun areItemsTheSame(oldItem: Excercise, newItem: Excercise): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Excercise,
        newItem: Excercise
    ): Boolean {
        return oldItem.name == newItem.name &&
                oldItem.equipment == newItem.equipment &&
                oldItem.difficulty == newItem.difficulty
    }

}