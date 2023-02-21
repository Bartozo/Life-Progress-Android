package com.bartozo.lifeprogress.repository

import com.bartozo.lifeprogress.data.Life

interface NotificationRepository {

    /**
     * Shows a weekly notification about current life progress.
     */
    fun showWeeklyNotification(life: Life)

    /**
     * Checks the ability to modify notification do not disturb policy for the calling package.
     */
    fun isNotificationPolicyAccessGranted(): Boolean

    /**
     * Checks if notifications are enabled and can be sent.
     */
    fun areNotificationsEnabled(): Boolean
}