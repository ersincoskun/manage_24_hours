package com.ersincoskun.manage24hours.viewmodel

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.ersincoskun.manage24hours.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

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

    fun notification(context: Context,activity: Activity){

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.add_icon)
            .setContentTitle("textTitle")
            .setContentText("textContent")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

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
                notify(1, builder.build())
            }
        }
    }

}