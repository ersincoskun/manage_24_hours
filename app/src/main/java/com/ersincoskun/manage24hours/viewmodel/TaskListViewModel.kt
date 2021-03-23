package com.ersincoskun.manage24hours.viewmodel

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
import kotlinx.coroutines.launch


class TaskListViewModel(context: Context) : ViewModel() {
    private val _task: MutableLiveData<Task> by lazy {
        MutableLiveData<Task>()
    }
    private val _allTask: MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    val task: LiveData<Task>
        get() = _task

    val allTask: MutableLiveData<List<Task>>
        get() = _allTask

    private val CHANNEL_ID: String by lazy {
        "channelID"
    }
    private val CHANNEL_NAME: String by lazy {
        "channelName"
    }

    init {
        getAllTask(context)
    }

    private fun getAllTask(context: Context) {
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            allTask.value = dao.getAllTask()
        }
    }

    fun updateList(context: Context, newList: MutableList<Task>) {
        viewModelScope.launch {
            val dao = TaskDatabase(context).taskDao()
            dao.deleteAllTasks()
            dao.insertAll(*newList.toTypedArray())
        }
    }

    fun notification(context: Context, activity: Activity) {

        val pendingIntent =
            NavDeepLinkBuilder(context)
                .setComponentName(MainActivity::class.java)
                .setDestination(R.id.taskListFragment)
                .setGraph(R.navigation.nav)
                .createPendingIntent()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.add_icon)
            .setContentTitle("textTitle")
            .setContentText("timer++.toString()")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance by lazy {
                NotificationManager.IMPORTANCE_DEFAULT
            }

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
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            with(NotificationManagerCompat.from(context)) {
                notify(1, builder)
            }
        }

    }

}