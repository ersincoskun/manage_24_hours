package com.ersincoskun.manage24hours.viewmodel

import android.app.*
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import com.ersincoskun.manage24hours.view.MainActivity
import com.ersincoskun.manage24hours.R
import com.ersincoskun.manage24hours.model.Task
import com.ersincoskun.manage24hours.service.TaskDatabase
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch

class AddTaskViewModel : ViewModel() {
    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"

    fun showTimePicker(fragmentManager: FragmentManager, editText: TextInputEditText) {
        val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(12)
            .setMinute(15).setTitleText("Select Start Time").build()

        picker.show(fragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            editText.setText("${if (picker.hour < 10) "0${picker.hour}" else picker.hour}:${if (picker.minute < 10) "0${picker.minute}" else picker.minute}")
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
            .setContentText("textContent")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "descriptionText"

            }
            val notificationManager: NotificationManager =
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            with(NotificationManagerCompat.from(context)) {
                notify(1, builder)
            }
        }
    }


    fun storeTaskInSQLite(context: Context, task: Task) {
        viewModelScope.launch {
            val dao = TaskDatabase.invoke(context).taskDao()
            val id = dao.insertTask(task)
            task.uuid = id


        }
    }

    fun validation(editText: TextInputEditText, editTextLayout: TextInputLayout): Boolean {
        var validation = false
        if (editText.text.isNullOrEmpty()) {
            editTextLayout.error =
                "it must not be empty"
            validation = false
        } else {
            validation = true
            editTextLayout.error = null
        }

        return validation
    }

    fun calculateTimeTake(time: String): String {
        val timeList = time.split(":")
        var hour = 0
        var minute = 0
        val startHour = timeList[0].toInt()
        val startMinute = timeList[1].toInt()
        val endHour = timeList[2].toInt()
        val endMinute = timeList[3].toInt()
        when {
            startHour > endHour -> {
                if(startMinute!=0){
                    hour=(23-startHour)+endHour
                    minute=(60-startMinute)+endMinute
                }
                else {
                    hour=(24-startHour)+endHour
                    minute=endMinute
                }
            }
            startHour < endHour -> {
                if(startMinute>endMinute) {
                    hour=(endHour-1)-startHour
                    minute=(60-startMinute)+endMinute
                }
                else {
                    hour=endHour-startHour
                    minute=startMinute+endMinute
                }
            }
            startHour==endHour->{
                hour=0
                if(startMinute!=0){
                    hour=(23-startHour)+endHour
                    minute=(60-startMinute)+endMinute
                }
                else {
                    hour=(24-startHour)+endHour
                    minute=endMinute
                }
            }
        }

        return "$hour:$minute"
    }

}