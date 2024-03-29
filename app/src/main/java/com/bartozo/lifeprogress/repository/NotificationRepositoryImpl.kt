package com.bartozo.lifeprogress.repository

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bartozo.lifeprogress.MainActivity
import com.bartozo.lifeprogress.R
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

        if (NotificationManagerCompat.from(appContext).areNotificationsEnabled()) {
            NotificationManagerCompat.from(appContext).notify(notificationId, notification)
        }
    }

    override fun isNotificationPolicyAccessGranted(): Boolean {
        val notificationManager = appContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return notificationManager.isNotificationPolicyAccessGranted
    }

    override fun areNotificationsEnabled() = NotificationManagerCompat.from(appContext)
        .areNotificationsEnabled()

    private fun createWeeklyNotificationChannel() {
        val name = appContext.getString(R.string.weekly_notification_channel_name)
        val descriptionText = appContext.getString(R.string.weekly_notification_channel_description)
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
        val pendingIntent = PendingIntent
            .getActivity(appContext, 0, intent, FLAG_IMMUTABLE)

        return NotificationCompat.Builder(appContext, weeklyChannelId)
            .setContentTitle(appContext.getString(R.string.weekly_notification_title))
            .setContentText(
                appContext.getString(R.string.life_progress, life.formattedProgress) +
                        " " +
                appContext.getString(R.string.year_progress, life.formattedCurrentYearProgress)
            )
            .setSmallIcon(R.drawable.life_progress_app_icon)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
    }
}