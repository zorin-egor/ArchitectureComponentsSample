package com.sample.architecturecomponents.core.common.extensions

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ShareCompat
import androidx.core.net.toUri

enum class MimeType(val type: String) {
    MessageRfc("message/rfc822"),
    TextAll("text/*"),
    TextPlain("text/plain"),
    TextRtf("text/rtf"),
    TextHtml("text/html"),
    TextJson("text/json"),
    ImageAll("`image/*`"),
    ImageJpg("image/jpg"),
    ImagePng("image/png"),
    ImageGif("image/gif"),
    VideoAll("video/*"),
    VideoMp4("video/mp4"),
    Video3gp("video/3gp"),
    ApplicationPdf("application/pdf");
}


fun Context.wipeAppData() {
    (getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)?.clearApplicationUserData()
}

fun getEmailIntent(
    address: String,
    subject: String = "",
    body: String = ""
): Intent = Intent(Intent.ACTION_SENDTO).apply {
    data = Uri.parse("mailto:")
    putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
    putExtra(Intent.EXTRA_SUBJECT, subject)
    putExtra(Intent.EXTRA_TEXT, body)
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
        body = body
    ), title))
}

val Context.settingsIntent: Intent
    get() = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

fun Context.openSettings() = kotlin.runCatching { startActivity(settingsIntent) }

fun getBrowserIntent(url: String, title: String? = null): Intent =
    Intent.createChooser(Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }, title)

fun Context.showBrowser(url: String, title: String? = null) {
    startActivity(getBrowserIntent(url, title))
}

fun Context.getShareIntent(
    body: String = "",
    title: String? = null,
    uri: String? = null,
    messageType: String = MimeType.MessageRfc.type
): Intent = ShareCompat.IntentBuilder(this)
    .setType(messageType)
    .setText(body)
    .setChooserTitle(title)
    .setStream(uri?.toUri())
    .apply {
        if (title?.isNotEmpty() == true) {
            intent.putExtra(Intent.EXTRA_TITLE, title)
        }
    }
    .createChooserIntent()

inline fun <reified T : Activity> Context.restartApp(isExit: Boolean = false) {
    startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    })
    if (isExit) {
        Runtime.getRuntime().exit(0)
    }
}