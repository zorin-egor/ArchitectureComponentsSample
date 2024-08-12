package com.sample.architecturecomponents.core.common.extensions

import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ShareCompat


fun Context.wipeAppData() {
    (getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)?.clearApplicationUserData()
}

fun Context.getEmailIntent(
    address: String,
    subject: String = "",
    body: String = "",
    title: String = ""
): Intent {
    return Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
}

fun Context.showEmail(
    address: String,
    subject: String = "",
    body: String = "",
    title: String = ""
) {
    startActivity(Intent.createChooser(getEmailIntent(
        address = address,
        subject = subject,
        body = body,
        title = title
    ), title))
}

fun Context.openSettings() {
    try {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    } catch (e: ActivityNotFoundException) {
        // Stub
    }
}

fun Context.showBrowser(url: String, title: String? = null) {
    startActivity(Intent.createChooser(Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }, title))
}

fun Context.showShare(
    address: String,
    subject: String = "",
    body: String = "",
    uri: Uri? = null,
    title: String = ""
) {
    ShareCompat.IntentBuilder(this)
        .setType("message/rfc822")
        .setEmailTo(arrayOf(address))
        .setStream(uri)
        .setSubject(subject)
        .setText(body)
        .setChooserTitle(title)
        .startChooser()
}

inline fun <reified T : Activity> Context.restartApp(isExit: Boolean = false) {
    startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    })
    if (isExit) {
        Runtime.getRuntime().exit(0)
    }
}