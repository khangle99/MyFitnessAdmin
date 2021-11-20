package com.khangle.myfitnessadmin.suggestpack.planlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessadmin.R
import com.khangle.myfitnessadmin.model.Plan

class PlanAdapter(val onItemclick: (item: Plan) -> Unit):
    ListAdapter<Plan, PlanAdapter.PlanVH>(planDiffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_plan, parent, false)
        return PlanVH(view, onItemclick)
    }

    override fun onBindViewHolder(holder: PlanVH, position: Int) {
        holder.bind(getItem(position))
    }

    class PlanVH(itemView: View, val onItemclick: (item: Plan) -> Unit) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView
        lateinit var item: Plan
        init {
            textView = itemView.findViewById(R.id.planDes)
            itemView.findViewById<CardView>(R.id.cardItem).setOnClickListener {
                onItemclick(item)
            }
        }
        fun bind(plan: Plan) {
            item = plan
            textView.text = plan.description
        }
    }
}
val planDiffUtil = object : DiffUtil.ItemCallback<Plan>() {
    override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Plan,
        newItem: Plan
    ): Boolean {
        return (oldItem.description == newItem.description)
    }

}
