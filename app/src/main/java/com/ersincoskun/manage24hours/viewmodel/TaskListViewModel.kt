package com.ersincoskun.manage24hours.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.service.TaskDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TaskListViewModel(val context: Context) : ViewModel() {

    private val _task: MutableLiveData<Task> by lazy {
        MutableLiveData<Task>()
    }

    private val _allTask: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    val task: LiveData<Task>
        get() = _task

    val allTask: LiveData<List<Task>>
        get() = _allTask

    fun getAllTask() {
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            _allTask.value = dao.getAllTask()
        }
    }

    fun updateList(newList: MutableList<Task>) {
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            dao.deleteAllTasks()
            dao.insertAll(*newList.toTypedArray())
        }
    }

}