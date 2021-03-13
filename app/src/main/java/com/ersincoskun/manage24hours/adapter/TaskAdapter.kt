package com.ersincoskun.manage24hours.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.ersincoskun.manage24hours.R
import com.ersincoskun.manage24hours.databinding.ItemTaskBinding
import com.ersincoskun.manage24hours.model.Task
import java.util.*

class TaskAdapter(var list: List<Task>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorList = listOf(
            Color.parseColor("#fc5444"),
            Color.parseColor("#fc9c44"),
            Color.parseColor("#fcc40c"),
            Color.parseColor("#44a4fc"),
            Color.parseColor("#24c46c")
        )
        holder.binding.titleTv.text = list[position].title
        holder.binding.descriptionTv.text = list[position].comment
        holder.binding.startAndEndTimeTv.text =
            "${list[position].startTime} - ${list[position].endTime}"
        holder.binding.itemCardview.setBackgroundColor(colorList[position % 5])
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun addTask(cameList: List<Task>) {
        list = cameList
        notifyDataSetChanged()
    }

}

class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)



