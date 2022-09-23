package com.bartozo.lifeprogress.ui.appwidgets

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AppWidgetPinnedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(
            context,
            "Widget pinned. Go to homescreen.",
            Toast.LENGTH_SHORT
        ).show()
    }
}