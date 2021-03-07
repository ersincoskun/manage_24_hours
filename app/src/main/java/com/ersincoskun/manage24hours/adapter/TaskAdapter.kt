package com.ersincoskun.manage24hours.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ersincoskun.manage24hours.databinding.ItemTaskBinding
import com.ersincoskun.manage24hours.model.Task

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
        holder.binding.titleTv.text = list[position].title
        holder.binding.descriptionTv.text = list[position].comment
        holder.binding.clockTv.text = list[position].startTime
        holder.binding.taskTimeTv.text=list[position].endTime
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addTask(cameList:List<Task>){
        notifyDataSetChanged()
        list=cameList
    }

}

class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)



