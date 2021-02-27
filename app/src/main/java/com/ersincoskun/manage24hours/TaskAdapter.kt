package com.ersincoskun.manage24hours

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ersincoskun.manage24hours.databinding.ItemTaskBinding
import com.ersincoskun.manage24hours.model.Task

class TaskAdapter(val list: List<Task>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.titleTv.text = list[position].title
        holder.binding.descriptionTv.text = list[position].description
        holder.binding.clockTv.text = list[position].clock

        holder.binding.checkBox.setOnClickListener {
            if (holder.binding.checkBox.isChecked) {
               TODO()
            } else {
                TODO()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)



