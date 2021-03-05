package com.ersincoskun.manage24hours.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ersincoskun.manage24hours.model.Task

class TaskListViewModel : ViewModel() {
    val tasks = MutableLiveData<List<Task>>()

    fun addTask() {
        tasks.value = listOf<Task>(
            Task("exapmle 1", "asdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasaasdaasdadsdsadsasa", "12:22AM"),
            Task("exapmle 2", "aasdada", "12:00PM"),
            Task("exapmle 3", "dfgfhfgfj", "08:12AM"),
            Task("exapmle 4", "asdasd", "12:22AM"),
            Task("exapmle 5", "gjfjfg", "12:22AM"),
        )
    }
}