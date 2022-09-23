package com.bartozo.lifeprogress.ui.appwidgets

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class LifeProgressWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LifeProgressWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        LifeProgressWorker.enqueue(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        LifeProgressWorker.cancel(context)
    }
}