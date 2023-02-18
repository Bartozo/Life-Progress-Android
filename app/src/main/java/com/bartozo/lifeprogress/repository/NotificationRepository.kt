package com.bartozo.lifeprogress.repository

import com.bartozo.lifeprogress.data.Life

interface NotificationRepository {

    /**
     * Shows a weekly notification about current life progress.
     */
    fun showWeeklyNotification(life: Life)
}