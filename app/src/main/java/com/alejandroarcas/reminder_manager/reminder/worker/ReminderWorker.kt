package com.alejandroarcas.reminder_manager.reminder.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log

import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.alejandroarcas.ReminderManager.R
import kotlinx.coroutines.delay

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

        override suspend fun doWork(): Result {
        return try {
            val title = inputData.getString("title") ?: "Reminder"

            setForeground(getForegroundInfo(title))

            //showNotification(title)

            Log.i("ReminderWorker", "Working...")

            delay(100000)

            Result.success()

        } catch (e: Exception) {
            val outputData = workDataOf(
                "error_code" to 1,
                "error_message" to e.message)

            Log.e("ReminderWorker", e.message ?: "Error")
             Result.failure(outputData)
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getForegroundInfo("Reminder")
    }

    private fun getForegroundInfo(title: String): ForegroundInfo{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(1, showNotification(title = title),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
            )
        } else {
            ForegroundInfo(1, showNotification(title = title))
        }
    }

    private fun showNotification(title: String): Notification {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "reminder_channel"
        val channelName = "Reminder Notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                description = "Channel for Reminder Notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Icono que debes agregar a tus recursos
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(false)
            .setAutoCancel(true)
            .build()

        Log.d("ReminderWorker", "Notification created")

        return notification
    }
}