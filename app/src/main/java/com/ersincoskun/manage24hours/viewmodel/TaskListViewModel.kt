package com.ersincoskun.manage24hours.viewmodel

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import com.ersincoskun.manage24hours.R
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.service.TaskDatabase
import com.ersincoskun.manage24hours.view.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class TaskListViewModel(val context: Context) : ViewModel() {

    private val _task: MutableLiveData<Task> by lazy {
        MutableLiveData<Task>()
    }

    private val _allTask: MutableLiveData<MutableList<Task>> by lazy {
        MutableLiveData<MutableList<Task>>()
    }

    init {
        if (_allTask.value == null) {
            getAllTask()
        }
    }

    val task: LiveData<Task>
        get() = _task

    val allTask: LiveData<MutableList<Task>>
        get() = _allTask

    fun getAllTask() {
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            _allTask.value = dao.getAllTask()
        }
    }

    fun updateList(from: Int, to: Int) {
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            dao.deleteAllTasks()
            val newList = _allTask.value
            Collections.swap(newList, from, to)
            newList!!.map {
                it.uuid = 0
            }
            dao.insertAll(*newList.toTypedArray())
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            dao.deleteTask(taskId)
        }
    }

}