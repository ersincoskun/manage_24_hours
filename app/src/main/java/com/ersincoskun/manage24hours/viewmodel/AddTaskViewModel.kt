package com.ersincoskun.manage24hours.viewmodel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.ersincoskun.manage24hours.R
import com.ersincoskun.manage24hours.adapter.NotificationWorker
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.service.TaskDatabase
import com.ersincoskun.manage24hours.view.MainActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class AddTaskViewModel : ViewModel() {

    private val CHANNEL_ID: String by lazy {
        "channelID"
    }
    private val CHANNEL_NAME: String by lazy {
        "channelName"
    }

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

    fun setWorker(task: Task, content: String, context: Context) {
        val data = Data.Builder().putString("content", content).build()
        val timeList = calculateTime(task.startTime).split(":")
        var time: Long = (timeList[0].toLong() * 60) + timeList[1].toLong()
        if (time > 15) {
            val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(time, TimeUnit.MINUTES)
                .setInputData(data)
                .build()
            WorkManager.getInstance(context).enqueue(myWorkRequest)
        } else {
            createNotification(context, "Görev ${time.toString()} dakika sonra başlayacak")
        }
    }

    private fun calculateTime(taskTime: String): String {
        val timeLeftToStart = calculateTimeLeftToStart(taskTime)
        val timeList = timeLeftToStart.split(":")
        val newMinute = timeList[1].toInt() % 60
        val plusHour = timeList[1].toInt() / 60
        val newHour = timeList[0].toInt() + plusHour
        val newTime = "$newHour:$newMinute"
        return newTime
    }

    fun calculateTimeLeftToStart(taskTime: String): String {
        val taskTimeList: List<String> by lazy {
            taskTime.split(":")
        }
        val currentTimeList: List<String> by lazy {
            Calendar.getInstance().time.toString().split(" ")[3].split(":")
        }
        val taskHour: Int by lazy {
            taskTimeList[0].toInt()
        }
        val taskMinutete: Int by lazy {
            taskTimeList[1].toInt()
        }
        val currentHour: Int by lazy {
            currentTimeList[0].toInt()
        }
        val currentMinute: Int by lazy {
            currentTimeList[1].toInt()
        }

        var hour = 0
        var minute = 0

        when {
            currentHour > taskHour -> {
                if (currentMinute != 0) {
                    hour = (23 - currentHour) + taskHour
                    minute = (60 - currentMinute) + taskMinutete
                } else {
                    hour = (24 - currentHour) + taskHour
                    minute = taskMinutete
                }
            }
            currentHour < taskHour -> {
                if (currentMinute > taskMinutete) {
                    hour = (taskHour - 1) - currentHour
                    minute = (60 - currentMinute) + taskMinutete
                } else {
                    hour = taskHour - currentHour
                    minute = currentMinute + taskMinutete
                }
            }
            currentHour == taskHour -> {
                hour = 0
                if(currentMinute<taskMinutete){
                    minute=taskMinutete-currentMinute
                }else{
                    hour=23
                    minute=60-(currentMinute-taskMinutete)
                }
            }
        }

        return "$hour:$minute"
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

    private fun calculateTimeTake(time: String): String {
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

    private fun createNotification(context: Context, content: String) {
        val pendingIntent =
            NavDeepLinkBuilder(context)
                .setComponentName(MainActivity::class.java)
                .setDestination(R.id.taskListFragment)
                .setGraph(R.navigation.nav)
                .createPendingIntent()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.add_icon)
            .setContentTitle("Hatırlatıcı")
            .setContentText(content)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel: NotificationChannel by lazy {
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "descriptionText"
                }
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            with(NotificationManagerCompat.from(context)) {
                notify(1, builder)
            }
        }

    }

}