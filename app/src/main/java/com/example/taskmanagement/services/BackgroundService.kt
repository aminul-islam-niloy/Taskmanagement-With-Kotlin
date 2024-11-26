//package com.example.taskmanagement.notifications
//
//import android.app.Service
//import android.content.Intent
//import android.os.IBinder
//import android.util.Log
//import com.example.taskmanagement.utils.TaskManagementData
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//class BackgroundService : Service() {
//
//    private lateinit var databaseRef: DatabaseReference
//    private lateinit var notificationHelper: NotificationHelper
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Log.d("BackgroundService", "Service started")
//
//        val auth = FirebaseAuth.getInstance()
//        val userId = auth.currentUser?.uid
//
//        if (userId == null) {
//            Log.e("BackgroundService", "User not authenticated")
//            stopSelf()
//            return START_NOT_STICKY
//        }
//
//        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks").child(userId)
//        notificationHelper = NotificationHelper(this)
//
//        monitorTasksForNotification()
//        return START_STICKY
//    }
//
//    private fun monitorTasksForNotification() {
//        Log.d("BackgroundService", "Monitoring tasks for notifications")
//
//        databaseRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val currentDateTime = System.currentTimeMillis()
//                Log.d("BackgroundService", "Checking tasks at $currentDateTime")
//
//                for (taskSnapshot in snapshot.children) {
//                    val taskData = taskSnapshot.getValue(TaskManagementData::class.java)
//                    if (taskData == null) {
//                        Log.d("BackgroundService", "Task data is null for snapshot: $taskSnapshot")
//                        continue
//                    }
//
//                    Log.d("BackgroundService", "Processing task: ${taskData.task}")
//
//                    val taskEndDateTime = try {
//                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//                            .parse(taskData.endDateTime)?.time ?: 0L
//                    } catch (e: Exception) {
//                        Log.e("BackgroundService", "Error parsing date for task: ${taskData.task}", e)
//                        continue
//                    }
//
//                    Log.d("BackgroundService", "Task end time: $taskEndDateTime")
//
//                    // Trigger notification if task is within the time window and not completed
//                    if (taskEndDateTime in (currentDateTime..currentDateTime + 60000) && !taskData.isCompleted) {
//                        Log.d("BackgroundService", "Triggering notification for task: ${taskData.task}")
//                        notificationHelper.sendNotification(
//                            taskData.task,
//                            "Task deadline is now: ${taskData.endDateTime}"
//                        )
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("BackgroundService", "Database error: ${error.message}")
//            }
//        })
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//}

//
//package com.example.taskmanagement.notifications
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.Service
//import android.content.Intent
//import android.os.Build
//import android.os.IBinder
//import android.util.Log
//import com.example.taskmanagement.utils.TaskManagementData
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//class BackgroundService : Service() {
//
//    private lateinit var databaseRef: DatabaseReference
//    private lateinit var notificationHelper: NotificationHelper
//
//    // Notification channel ID for Android 8.0 and above
//    private val notificationChannelId = "task_notifications"
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Log.d("BackgroundService", "Service started")
//
//        // Firebase authentication to get current user ID
//        val auth = FirebaseAuth.getInstance()
//        val userId = auth.currentUser?.uid
//
//        if (userId == null) {
//            Log.e("BackgroundService", "User not authenticated")
//            stopSelf()
//            return START_NOT_STICKY
//        }
//
//        // Database reference pointing to the user's tasks
//        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks").child(userId)
//        notificationHelper = NotificationHelper(this)
//
//        // Ensure notification channel is created (for Android 8.0 and above)
//        createNotificationChannel()
//
//        // Start monitoring tasks for notifications
//        monitorTasksForNotification()
//        return START_STICKY
//    }
//
//    // Create notification channel (required for Android 8.0 and above)
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                notificationChannelId,
//                "Task Notifications",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val notificationManager = getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    // Monitor tasks from Firebase Database for upcoming deadlines and trigger notifications
//    private fun monitorTasksForNotification() {
//        Log.d("BackgroundService", "Monitoring tasks for notifications")
//
//        databaseRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val currentDateTime = System.currentTimeMillis()
//                Log.d("BackgroundService", "Checking tasks at $currentDateTime")
//
//                for (taskSnapshot in snapshot.children) {
//                    val taskData = taskSnapshot.getValue(TaskManagementData::class.java)
//                    if (taskData == null) {
//                        Log.d("BackgroundService", "Task data is null for snapshot: $taskSnapshot")
//                        continue
//                    }
//
//                    Log.d("BackgroundService", "Processing task: ${taskData.task}")
//
//                    val taskEndDateTime = try {
//                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//                            .parse(taskData.endDateTime)?.time ?: 0L
//                    } catch (e: Exception) {
//                        Log.e("BackgroundService", "Error parsing date for task: ${taskData.task}", e)
//                        continue
//                    }
//
//                    Log.d("BackgroundService", "Task end time: $taskEndDateTime")
//
//                    // Trigger notification if task is within the next 5 minutes and not completed
//                    if (taskEndDateTime in (currentDateTime..currentDateTime + 5 * 60 * 1000) && !taskData.isCompleted) {
//                        Log.d("BackgroundService", "Triggering notification for task: ${taskData.task}")
//                        notificationHelper.sendNotification(
//                            taskData.task,
//                            "Task deadline is now: ${taskData.endDateTime}"
//                        )
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("BackgroundService", "Database error: ${error.message}")
//            }
//        })
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//}





package com.example.taskmanagement.notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.taskmanagement.utils.TaskManagementData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class BackgroundService : Service() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var notificationHelper: NotificationHelper

    // A set to track tasks for which notifications have already been sent
    private val notifiedTasks = mutableSetOf<String>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("BackgroundService", "Service started")

        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Log.e("BackgroundService", "User not authenticated")
            stopSelf()
            return START_NOT_STICKY
        }

        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks").child(userId)
        notificationHelper = NotificationHelper(this)

        monitorTasksForNotification()
        return START_STICKY
    }

    private fun monitorTasksForNotification() {
        Log.d("BackgroundService", "Monitoring tasks for notifications")

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentDateTime = System.currentTimeMillis()
                Log.d("BackgroundService", "Checking tasks at $currentDateTime")

                for (taskSnapshot in snapshot.children) {
                    val taskData = taskSnapshot.getValue(TaskManagementData::class.java)
                    if (taskData == null) {
                        Log.d("BackgroundService", "Task data is null for snapshot: $taskSnapshot")
                        continue
                    }

                    Log.d("BackgroundService", "Processing task: ${taskData.task}")

                    val taskEndDateTime = try {
                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            .parse(taskData.endDateTime)?.time ?: 0L
                    } catch (e: Exception) {
                        Log.e("BackgroundService", "Error parsing date for task: ${taskData.task}", e)
                        continue
                    }

                    if (!taskData.isCompleted && taskEndDateTime > 0) {
                        val timeLeft = taskEndDateTime - currentDateTime
                        if (timeLeft in 0..300000) { // Notify if within 5 minutes
                            if (!notifiedTasks.contains(taskData.taskId)) {
                                Log.d("BackgroundService", "Triggering notification for task: ${taskData.task}")
                                notificationHelper.sendNotification(
                                    taskData.task,
                                    "Task deadline is now: ${taskData.endDateTime}"
                                )
                                notifiedTasks.add(taskData.taskId)
                            }
                        } else if (timeLeft < 0) {
                            Log.d("BackgroundService", "Task missed: ${taskData.task}")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BackgroundService", "Database error: ${error.message}")
            }
        })
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
