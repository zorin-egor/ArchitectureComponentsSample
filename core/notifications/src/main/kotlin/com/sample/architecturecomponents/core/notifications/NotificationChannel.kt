package com.sample.architecturecomponents.core.notifications

import androidx.annotation.StringRes
import com.sample.architecturecomponents.core.notification.R

enum class NotificationChannel(val id: String, @StringRes val description: Int, ) {
    Common("common_notification_channel", R.string.core_notifications_channel_common),
    Action("action_notification_channel", R.string.core_notifications_channel_action),
    Image("image_notification_channel", R.string.core_notifications_channel_image);
}