package com.example.taskmanagement.notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.taskmanagement.utils.TaskManagementData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Locale

class BackgroundService : Service() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var notificationHelper: NotificationHelper

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

                    Log.d("BackgroundService", "Task end time: $taskEndDateTime")

                    // Trigger notification if task is within the time window and not completed
                    if (taskEndDateTime in (currentDateTime..currentDateTime + 60000) && !taskData.isCompleted) {
                        Log.d("BackgroundService", "Triggering notification for task: ${taskData.task}")
                        notificationHelper.sendNotification(
                            taskData.task,
                            "Task deadline is now: ${taskData.endDateTime}"
                        )
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
