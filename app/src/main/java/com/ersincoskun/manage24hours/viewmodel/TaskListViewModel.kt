package com.ersincoskun.manage24hours.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ersincoskun.manage24hours.adapter.TaskAdapter
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.service.TaskDatabase
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    val task = MutableLiveData<Task>()
    val allTask=MutableLiveData<MutableList<Task>>()

    fun getTask(context: Context, id: Long) {
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            task.value = dao.getTask(id)
        }
    }
    fun getAllTask(context: Context){
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            allTask.value = dao.getAllTask()
        }
    }

    fun updateList(context: Context,newList: MutableList<Task>){
        viewModelScope.launch {
            val dao=TaskDatabase(context).taskDao()
            dao.deleteAllTasks()
            allTask.value=newList
        }
    }

    fun addListToAdapter(adapter:TaskAdapter,list: MutableList<Task>){
        adapter.addTask(list)
    }

}