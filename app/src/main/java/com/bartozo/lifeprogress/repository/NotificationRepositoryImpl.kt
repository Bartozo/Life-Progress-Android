package com.bartozo.lifeprogress.repository

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bartozo.lifeprogress.MainActivity
import com.bartozo.lifeprogress.data.Life
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    @ApplicationContext val appContext: Context,
) : NotificationRepository {

    companion object {
        const val weeklyChannelId = "WEEKLY_NOTIFICATION_CHANNEL_ID"
    }

    override fun showWeeklyNotification(life: Life) {
        createWeeklyNotificationChannel()

        val notification = buildWeeklyNotification(life = life)
        val notificationId = System.currentTimeMillis().toInt()

        NotificationManagerCompat.from(appContext).notify(notificationId, notification)
    }

    private fun createWeeklyNotificationChannel() {
        val name = "Weekly Notification Channel"
        val descriptionText = "Receive weekly notification with current life progress"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(weeklyChannelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager = appContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun buildWeeklyNotification(life: Life): Notification {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0)

        return NotificationCompat.Builder(appContext, weeklyChannelId)
            .setContentTitle("Your weekly Life Progress")
            .setContentText(
                "Life Progress: ${life.formattedProgress}," +
                        " Year Progress: ${life.formattedCurrentYearProgress}"
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}