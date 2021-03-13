package com.ersincoskun.manage24hours.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ersincoskun.manage24hours.adapter.TaskAdapter
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.service.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class TaskListViewModel : ViewModel() {
    val task: MutableLiveData<Task> by lazy {
        MutableLiveData<Task>()
    }
    val allTask: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    fun getAllTask(context: Context) {
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            allTask.value = dao.getAllTask()

        }
    }

    fun updateList(context: Context, newList: MutableList<Task>) {
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            newList.forEach {
                System.out.println(it.title)
            }
            dao.deleteAllTasks()
            dao.insertAll(*newList.toTypedArray())
        }
    }


}