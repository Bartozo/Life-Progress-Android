package com.bartozo.lifeprogress.worker

import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.repository.NotificationRepository
import com.bartozo.lifeprogress.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.Duration

@HiltWorker
class WeeklyNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository
) : CoroutineWorker(context, workerParameters) {

    companion object {

        private val uniqueWorkName = WeeklyNotificationWorker::class.java.simpleName

        /**
         * Enqueues a new worker to display a weekly life progress notification only if not
         * enqueued already.
         * @param force set to true to replace any ongoing work and expedite the request.
         */
        fun enqueue(context: Context, force: Boolean = false) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder = PeriodicWorkRequestBuilder<WeeklyNotificationWorker>(
                Duration.ofDays(7)
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
        return try {
            // Get the data required to show current life progress
            val birthday = userRepository.birthDay.first()
            val lifeExpectancy = userRepository.lifeExpectancy.first()
            val life = Life.invoke(birthday = birthday, lifeExpectancy = lifeExpectancy)

            // Show notification
            notificationRepository.showWeeklyNotification(life = life)

            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 10) {
                // Exponential backoff strategy will avoid the request to repeat
                // too fast in case of failures.
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}