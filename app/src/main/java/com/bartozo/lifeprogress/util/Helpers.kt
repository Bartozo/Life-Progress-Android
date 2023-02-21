package com.bartozo.lifeprogress.util

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat

fun Context.sendMail(
    to: String,
    subject: String,
    onError: () -> Unit
) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)

        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        onError.invoke()
    } catch (t: Throwable) {
        onError.invoke()
    }
}

fun Context.hasNotificationPermission(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        val result = ContextCompat.checkSelfPermission(this, permission)

        return result == PackageManager.PERMISSION_GRANTED
    }

    return true
}
