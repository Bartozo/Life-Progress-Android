package com.bartozo.lifeprogress.ui.appwidgets

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.data.LifeState
import com.bartozo.lifeprogress.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.Duration

@HiltWorker
class LifeProgressWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(context, workerParameters) {

    companion object {

        private val uniqueWorkName = LifeProgressWorker::class.java.simpleName

        /**
         * Enqueues a new worker to refresh life calendar data only if not enqueued already
         * @param force set to true to replace any ongoing work and expedite the request
         */
        fun enqueue(context: Context, force: Boolean = false) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder = PeriodicWorkRequestBuilder<LifeProgressWorker>(
                Duration.ofDays(1)
            )
            var workPolicy = ExistingPeriodicWorkPolicy.KEEP

            // Replace any enqueued work and expedite the request
            if (force) {
                workPolicy = ExistingPeriodicWorkPolicy.REPLACE
            }

            manager.enqueueUniquePeriodicWork(
                uniqueWorkName,
                workPolicy,
                requestBuilder.build()
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)
        }
    }

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(LifeProgressWidget::class.java)
        return try {
            // Update state to indicate loading
            setWidgetState(glanceIds, LifeState.Loading)

            // Update state with new data
            val birthday = userRepository.birthDay.first()
            val lifeExpectancy = userRepository.lifeExpectancy.first()
            val life = Life.invoke(birthday = birthday, lifeExpectancy = lifeExpectancy)
            setWidgetState(
                glanceIds,
                LifeState.Available(
                    age = life.age,
                    lifeExpectancy = life.lifeExpectancy,
                    weekOfYear = life.weekOfYear
                )
            )

            Result.success()
        } catch (e: Exception) {
            setWidgetState(glanceIds, LifeState.Unavailable(e.message.orEmpty()))
            if (runAttemptCount < 10) {
                // Exponential backoff strategy will avoid the request to repeat
                // too fast in case of failures.
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    /**
     * Update the state of all widgets and then force update UI
     */
    private suspend fun setWidgetState(glanceIds: List<GlanceId>, lifeState: LifeState) {
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(
                context = context,
                definition = LifeProgressStateDefinition,
                glanceId = glanceId,
                updateState = { lifeState }
            )
        }
        LifeProgressWidget().updateAll(context)
    }
}