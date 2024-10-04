package com.sample.architecturecomponents.core.testing.tests.notifications

import android.content.Intent
import com.sample.architecturecomponents.core.notifications.NotificationChannel
import com.sample.architecturecomponents.core.notifications.NotificationConfig
import com.sample.architecturecomponents.core.notifications.NotificationImageSize
import com.sample.architecturecomponents.core.notifications.Notifier


class NotifierTest : Notifier {

    override fun postText(
        header: CharSequence,
        body: CharSequence,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = true

    override fun postTextAction(
        header: CharSequence,
        body: CharSequence,
        actionIcon: Int,
        actionText: CharSequence,
        actionIntent: Intent,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = true

    override fun postImageText(
        header: CharSequence,
        body: CharSequence,
        url: String,
        imageSize: NotificationImageSize,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = true

    override fun postProgress(
        header: CharSequence,
        maxProgress: Int,
        progress: Int,
        actionIcon: Int,
        actionText: CharSequence,
        actionIntent: Intent,
        isIntermediate: Boolean,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = true
}
