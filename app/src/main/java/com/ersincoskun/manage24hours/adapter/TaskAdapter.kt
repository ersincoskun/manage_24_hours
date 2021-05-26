package com.ersincoskun.manage24hours.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ersincoskun.manage24hours.R
import com.ersincoskun.manage24hours.databinding.ItemTaskBinding
import com.ersincoskun.manage24hours.model.Task

class TaskAdapter(var list: MutableList<Task>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bgList = listOf(
            R.drawable.bg1,
            R.drawable.bg2,
            R.drawable.bg3,
            R.drawable.bg4,
            R.drawable.bg5,
            R.drawable.bg6
        )
        holder.binding.titleTv.text = list[position].title
        holder.binding.descriptionTv.text = list[position].comment
        holder.binding.startAndEndTimeTv.text =
            "${list[position].startTime} - ${list[position].endTime}"

        holder.binding.itemCardView.setBackgroundResource(bgList[position % 6])

    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun addTask(cameList: MutableList<Task>) {
        list = cameList
        notifyDataSetChanged()
    }

    fun deleteTask(position: Int){
        list.removeAt(position)
        this.notifyItemRemoved(position)
    }

}

class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)



