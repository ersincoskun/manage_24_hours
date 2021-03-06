package com.ersincoskun.manage24hours.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.service.TaskDatabase
import kotlinx.coroutines.launch

class TaskListViewModel(val context: Context) : ViewModel() {
    val tasks = MutableLiveData<List<Task>>()

    fun getTaskFromDB(){
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            tasks.value=dao.getAllTask()
        }
    }
}