package com.ersincoskun.manage24hours.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ersincoskun.manage24hours.R
import com.ersincoskun.manage24hours.view.MainActivity
import kotlinx.coroutines.delay
import java.util.*

class NotificationWorker(
    val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(
    context,
    workerParams
) {

    private val CHANNEL_ID: String by lazy {
        "channelID"
    }
    private val CHANNEL_NAME: String by lazy {
        "channelName"
    }

    override suspend fun doWork(): Result {

        val getData = inputData
        val content = getData.getString("content")
        val content2 = getData.getString("content2")
        content?.let {
            notification(context, it)
        }
        content2?.let {
            notification(context, it)
        }
        return Result.success()
    }

    private fun notification(context: Context, content: String) {
        val pendingIntent =
            NavDeepLinkBuilder(context)
                .setComponentName(MainActivity::class.java)
                .setDestination(R.id.taskListFragment)
                .setGraph(R.navigation.nav)
                .createPendingIntent()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_time)
                .setContentTitle("Reminder")
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

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
        } else {
            val builder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_time)
                .setContentTitle("Reminder")
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            NotificationManagerCompat.from(context).notify(2, builder)
        }

    }

}

