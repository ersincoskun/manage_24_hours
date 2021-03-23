package com.ersincoskun.manage24hours.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.service.TaskDatabase
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch

class AddTaskViewModel : ViewModel() {

    fun storeTaskInSQLite(context: Context, task: Task) {
        viewModelScope.launch {
            val dao = TaskDatabase.invoke(context).taskDao()
            dao.insertTask(task)
        }
    }

    fun deleteAllTask(context: Context) {
        viewModelScope.launch {
            val dao = TaskDatabase.invoke(context).taskDao()
            dao.deleteAllTasks()
        }
    }

    fun validation(editTextLayout: TextInputLayout): Boolean {
        var validation = false
        if (editTextLayout.editText!!.text.isNullOrEmpty()) {
            editTextLayout.error =
                "it must not be empty"
            validation = false
        } else {
            validation = true
            editTextLayout.error = null
        }

        return validation
    }

    fun validationStartEndTime(startTime: TextInputLayout, endTime: TextInputLayout): Boolean {
        if (startTime.editText!!.text.toString() == endTime.editText!!.text.toString()) {
            startTime.error =
                "must not be equal to end time"
            return false
        } else {
            startTime.error = null
            return true
        }
    }

    fun timeTakeGenerator(startTime: String, endTime: String): String {
        val time = "${startTime}:${endTime}"
        val timeTake = calculateTimeTake(time).split(":")
        val newMinute = timeTake[1].toInt() % 60
        val plusHour = timeTake[1].toInt() / 60
        val newHour = timeTake[0].toInt() + plusHour
        val newTime =
            "${if (newHour < 10) "0$newHour" else "$newHour"}:${if (newMinute < 10) "0$newMinute" else "$newMinute"}:00"
        return newTime
    }

    fun showTimePicker(fragmentManager: FragmentManager, editText: TextInputEditText) {
        val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(12)
            .setMinute(15).setTitleText("Select Start Time").build()

        picker.show(fragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            editText.setText("${if (picker.hour < 10) "0${picker.hour}" else picker.hour}:${if (picker.minute < 10) "0${picker.minute}" else picker.minute}")
        }

    }

    fun calculateTimeTake(time: String): String {
        val timeList: List<String> by lazy {
            time.split(":")
        }
        val startHour: Int by lazy {
            timeList[0].toInt()
        }
        val startMinute: Int by lazy {
            timeList[1].toInt()
        }
        val endHour: Int by lazy {
            timeList[2].toInt()
        }
        val endMinute: Int by lazy {
            timeList[3].toInt()
        }

        var hour = 0
        var minute = 0

        when {
            startHour > endHour -> {
                if (startMinute != 0) {
                    hour = (23 - startHour) + endHour
                    minute = (60 - startMinute) + endMinute
                } else {
                    hour = (24 - startHour) + endHour
                    minute = endMinute
                }
            }
            startHour < endHour -> {
                if (startMinute > endMinute) {
                    hour = (endHour - 1) - startHour
                    minute = (60 - startMinute) + endMinute
                } else {
                    hour = endHour - startHour
                    minute = startMinute + endMinute
                }
            }
            startHour == endHour -> {
                hour = 0
                if (startMinute != 0) {
                    hour = (23 - startHour) + endHour
                    minute = (60 - startMinute) + endMinute
                } else {
                    hour = (24 - startHour) + endHour
                    minute = endMinute
                }
            }
        }

        return "$hour:$minute"
    }

}