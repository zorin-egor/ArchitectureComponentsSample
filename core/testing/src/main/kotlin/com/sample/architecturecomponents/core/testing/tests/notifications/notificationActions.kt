package com.sample.architecturecomponents.core.testing.tests.notifications

import android.content.Intent
import com.sample.architecturecomponents.core.notifications.NotificationActionsRes
import com.sample.architecturecomponents.core.notifications.NotificationChannel
import com.sample.architecturecomponents.core.notifications.NotificationConfig
import com.sample.architecturecomponents.core.notifications.NotificationImageSize

class NotificationActionsResTestImpl: NotificationActionsRes {

    override fun postText(
        header: Int,
        body: Int,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = true

    override fun postTextAction(
        header: Int,
        body: Int,
        actionIcon: Int,
        actionText: Int,
        actionIntent: Intent,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = true

    override fun postImageText(
        header: Int,
        body: Int,
        url: String,
        imageSize: NotificationImageSize,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = true

    override fun postProgress(
        header: Int,
        maxProgress: Int,
        progress: Int,
        actionIcon: Int,
        actionText: Int,
        actionIntent: Intent,
        isIntermediate: Boolean,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = true
}