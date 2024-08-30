package com.sample.architecturecomponents.core.notifications

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.sample.architecturecomponents.core.notification.R
import kotlin.random.Random

data class NotificationConfig(
    val id: Int = Random.nextInt(),
    val group: String? = null,
    val isGroupSummary: Boolean = false,
    val isAutoCancel: Boolean = true,
    val isOngoing: Boolean = false,
    val isOnlyAlertOnce: Boolean = false,
    val category: String = NotificationCompat.CATEGORY_MESSAGE,
    val tag: String? = id.toString(),
    val action: Intent? = null,
    val actionRequestCode: Int = Random.nextInt(),
    @DrawableRes val smallIcon: Int = R.drawable.core_notifications_ic_nia_notification,
    val builder: (NotificationCompat.Builder.() -> Unit) = {}
)