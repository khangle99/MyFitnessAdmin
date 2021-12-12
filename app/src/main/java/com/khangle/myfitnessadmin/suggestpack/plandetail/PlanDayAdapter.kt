package com.khangle.myfitnessadmin.suggestpack.plandetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.common.Difficulty
import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.PlanDay
import kotlin.time.nanoseconds


class PlanDayAdapter(val onItemclick: (item: PlanDay) -> Unit):
    ListAdapter<PlanDay, PlanDayAdapter.PlanDayVH>(planDayDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanDayVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_planday, parent, false)
        return PlanDayVH(view, onItemclick)
    }

    override fun onBindViewHolder(holder: PlanDayVH, position: Int) {
        holder.bind(getItem(position))
    }

    class PlanDayVH(itemView: View, val onItemclick: (item: PlanDay) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView
        val difficultyTv: TextView
        lateinit var item: PlanDay
        private val dayNameList = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        init {
            nameTv = itemView.findViewById(R.id.excerciseNameTv)
            difficultyTv = itemView.findViewById(R.id.excDifficultyTv)
            itemView.findViewById<CardView>(R.id.cardItem).setOnClickListener {
                onItemclick(item)
            }
        }
        fun bind(planDay: PlanDay) {
            item = planDay
            nameTv.text = planDay.exc?.name ?: "Deleted Excercise"
            difficultyTv.text = dayNameList.get(planDay.day.toInt())
        }
    }
}
val planDayDiffUtil = object : DiffUtil.ItemCallback<PlanDay>() {
    override fun areItemsTheSame(oldItem: PlanDay, newItem: PlanDay): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PlanDay,
        newItem: PlanDay
    ): Boolean {
        return oldItem.day == newItem.day &&
                oldItem.categoryId == newItem.categoryId &&
                oldItem.excId == newItem.excId
    }

}