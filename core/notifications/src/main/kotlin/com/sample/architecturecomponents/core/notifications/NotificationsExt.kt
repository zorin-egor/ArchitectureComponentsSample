package com.sample.architecturecomponents.core.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import androidx.core.app.NotificationCompat
import kotlin.random.Random

val Context.audioManager: AudioManager?
    get() = getSystemService(Context.AUDIO_SERVICE) as? AudioManager

val Context.notificationManager: NotificationManager?
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

fun NotificationCompat.Builder.showNotification(context: Context, id: Int, tag: String? = null) {
    context.notificationManager?.notify(tag, id, build())
}

fun Context.createNotificationChannel(
    id: String,
    name: String,
    @SuppressLint("InlinedApi") importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
    apply: (NotificationChannel.() -> Unit) = {}
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notificationManager?.createNotificationChannel(NotificationChannel(id, name, importance)
            .apply(apply))
    }
}

fun Context.removeNotification(id: Int, tag: String? = null) {
    notificationManager?.cancel(tag, id)
}

fun Context.getNotificationPendingIntent(intent: Intent, requestCode: Int = Random.nextInt()): PendingIntent {
    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_CANCEL_CURRENT
    }
    return PendingIntent.getActivity(this, requestCode, intent, flags)
}

fun NotificationCompat.Builder.defaultSettings(context: Context, config: NotificationConfig) {
    setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    setOngoing(config.isOngoing)
    setOnlyAlertOnce(config.isOnlyAlertOnce)
    setSmallIcon(config.smallIcon)
    setGroup(config.group)
    setGroupSummary(config.isGroupSummary)
    setCategory(config.category)
    setAutoCancel(config.isAutoCancel)
    setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
    setFullScreenIntent(null, true)

    if (config.action != null) {
        setContentIntent(context.getNotificationPendingIntent(
            intent = config.action,
            requestCode = config.actionRequestCode)
        )
    }

    val audioManager = context.audioManager ?: return
    val defaults = Notification.DEFAULT_LIGHTS or when (audioManager.ringerMode) {
        AudioManager.RINGER_MODE_VIBRATE -> Notification.DEFAULT_VIBRATE
        AudioManager.RINGER_MODE_NORMAL -> Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND
        else -> 0
    }
    setDefaults(defaults)
}