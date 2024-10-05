package com.sample.architecturecomponents.core.notifications

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


interface NotificationActionsRes {

    fun postText(
        @StringRes header: Int,
        @StringRes body: Int,
        channel: NotificationChannel = NotificationChannel.Common,
        config: NotificationConfig = NotificationConfig()
    ): Boolean

    fun postTextAction(
        @StringRes header: Int,
        @StringRes body: Int,
        actionIcon: Int,
        @StringRes actionText: Int,
        actionIntent: Intent,
        channel: NotificationChannel = NotificationChannel.Action,
        config: NotificationConfig = NotificationConfig()
    ): Boolean

    fun postImageText(
        @StringRes header: Int,
        @StringRes body: Int,
        url: String,
        imageSize: NotificationImageSize = NotificationImageSize.Minimum,
        channel: NotificationChannel = NotificationChannel.Image,
        config: NotificationConfig = NotificationConfig()
    ): Boolean

    fun postProgress(
        @StringRes header: Int,
        maxProgress: Int,
        progress: Int,
        @DrawableRes actionIcon: Int,
        @StringRes actionText: Int,
        actionIntent: Intent,
        isIntermediate: Boolean = false,
        channel: NotificationChannel = NotificationChannel.Image,
        config: NotificationConfig = NotificationConfig()
    ): Boolean
}

@Singleton
internal class NotificationActionsResImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notifier: NotificationActions
) : NotificationActionsRes {

    override fun postText(
        @StringRes header: Int,
        @StringRes body: Int,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = notifier.postText(
        header = context.getString(header),
        body = context.getString(body),
        channel = channel,
        config = config
    )

    override fun postTextAction(
        @StringRes header: Int,
        @StringRes body: Int,
        @DrawableRes actionIcon: Int,
        @StringRes actionText: Int,
        actionIntent: Intent,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = notifier.postTextAction(
        header = context.getString(header),
        body = context.getString(body),
        actionIcon = actionIcon,
        actionText = context.getString(actionText),
        actionIntent = actionIntent,
        channel = channel,
        config = config
    )

    override fun postImageText(
        @StringRes header: Int,
        @StringRes body: Int,
        url: String,
        imageSize: NotificationImageSize,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = notifier.postImageText(
        header = context.getString(header),
        body = context.getString(body),
        url = url,
        channel = channel,
        config = config,
        imageSize = imageSize
    )

    override fun postProgress(
        @StringRes header: Int,
        maxProgress: Int,
        progress: Int,
        @DrawableRes actionIcon: Int,
        @StringRes actionText: Int,
        actionIntent: Intent,
        isIntermediate: Boolean,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean = notifier.postProgress(
        header = context.getString(header),
        maxProgress = maxProgress,
        progress = progress,
        actionIcon = actionIcon,
        actionText = context.getString(actionText),
        actionIntent = actionIntent,
        isIntermediate = isIntermediate,
        channel = channel,
        config = config
    )

}

