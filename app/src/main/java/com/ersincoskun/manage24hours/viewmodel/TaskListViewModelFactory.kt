package com.ersincoskun.manage24hours.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ersincoskun.manage24hours.adapter.TaskAdapter

class TaskListViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
            return TaskListViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
