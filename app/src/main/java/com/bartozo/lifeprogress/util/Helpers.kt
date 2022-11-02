package com.bartozo.lifeprogress.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

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