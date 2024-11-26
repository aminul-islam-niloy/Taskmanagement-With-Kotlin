//package com.example.taskmanagement.notifications
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.os.Build
//import androidx.core.app.NotificationCompat
//import com.example.taskmanagement.R
//
//class NotificationHelper(private val context: Context) {
//
//    companion object {
//        private const val CHANNEL_ID = "task_notifications_channel"
//        private const val CHANNEL_NAME = "Task Notifications"
//        private const val CHANNEL_DESCRIPTION = "Channel for task deadline notifications"
//    }
//
//    init {
//        createNotificationChannel()
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_HIGH
//            ).apply {
//                description = CHANNEL_DESCRIPTION
//            }
//
//            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
//        }
//    }
//
//    fun sendNotification(title: String, message: String) {
//        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.drawable.baseline_notifications_24)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//            .build()
//
//        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        manager.notify((System.currentTimeMillis() / 1000).toInt(), notification)
//    }
//}


//package com.example.taskmanagement.notifications
//
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.os.Build
//import androidx.core.app.NotificationCompat
//import com.example.taskmanagement.R
//
//class NotificationHelper(private val context: Context) {
//
//    companion object {
//        private const val CHANNEL_ID = "task_notifications_channel"
//        private const val CHANNEL_NAME = "Task Notifications"
//        private const val CHANNEL_DESCRIPTION = "Channel for task deadline notifications"
//    }
//
//    init {
//        // Ensure the notification channel is created at the time of initialization
//        createNotificationChannel()
//    }
//
//    // Creates the notification channel (required for Android 8.0+)
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_HIGH
//            ).apply {
//                description = CHANNEL_DESCRIPTION
//            }
//
//            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
//        }
//    }
//
//    // Sends the notification with the specified title and message
//    fun sendNotification(title: String, message: String) {
//        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.drawable.baseline_notifications_24) // Ensure the icon exists
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_HIGH) // High priority for important notifications
//            .setAutoCancel(true) // Auto dismiss after tapping on it
//            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE) // Default sound & vibration
//            .build()
//
//        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        manager.notify((System.currentTimeMillis() / 1000).toInt(), notification) // Use a unique notification ID
//    }
//}



package com.example.taskmanagement.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.taskmanagement.R

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "task_notifications_channel"
        private const val CHANNEL_NAME = "Task Notifications"
        private const val CHANNEL_DESCRIPTION = "Notifications for task deadlines"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify((System.currentTimeMillis() / 1000).toInt(), notification)
    }
}
