package com.sample.architecturecomponents.core.notifications

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.NotificationCompat
import coil.ImageLoader
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


interface NotificationActions {

    fun postText(
        header: CharSequence,
        body: CharSequence,
        channel: NotificationChannel = NotificationChannel.Common,
        config: NotificationConfig = NotificationConfig()
    ): Boolean

    fun postTextAction(
        header: CharSequence,
        body: CharSequence,
        actionIcon: Int,
        actionText: CharSequence,
        actionIntent: Intent,
        channel: NotificationChannel = NotificationChannel.Action,
        config: NotificationConfig = NotificationConfig()
    ): Boolean

    fun postImageText(
        header: CharSequence,
        body: CharSequence,
        url: String,
        imageSize: NotificationImageSize = NotificationImageSize.Minimum,
        channel: NotificationChannel = NotificationChannel.Image,
        config: NotificationConfig = NotificationConfig()
    ): Boolean

    fun postProgress(
        header: CharSequence,
        maxProgress: Int,
        progress: Int,
        @DrawableRes actionIcon: Int,
        actionText: CharSequence,
        actionIntent: Intent,
        isIntermediate: Boolean = false,
        channel: NotificationChannel = NotificationChannel.Image,
        config: NotificationConfig = NotificationConfig()
    ): Boolean
}

@Singleton
internal class NotificationActionsImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageLoader: ImageLoader
) : NotificationActions {

    override fun postText(
        header: CharSequence,
        body: CharSequence,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean {
        if (checkSelfPermission(context, permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
            return false
        }

        context.createNotificationChannel(id = channel.id, context.getString(channel.description))
        val nb = NotificationCompat.Builder(context, channel.id)
        nb.defaultSettings(context = context, config = config)
        nb.apply(config.builder)
        nb.setContentTitle(header)
        nb.setContentText(body)
        nb.showNotification(context = context, tag = config.tag, id = config.id)
        return true
    }

    override fun postTextAction(
        header: CharSequence,
        body: CharSequence,
        @DrawableRes actionIcon: Int,
        actionText: CharSequence,
        actionIntent: Intent,
        channel: NotificationChannel,
        config: NotificationConfig
    ): Boolean {
        if (checkSelfPermission(context, permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
            return false
        }

        context.createNotificationChannel(id = channel.id, context.getString(channel.description))
        val actionIntentPending = context.getNotificationPendingIntent(actionIntent)
        val nb = NotificationCompat.Builder(context, channel.id)
        nb.defaultSettings(context = context, config = config)
        nb.apply(config.builder)
        nb.setContentTitle(header)
        nb.setContentText(body)
        nb.setDeleteIntent(actionIntentPending)
        nb.addAction(
            actionIcon,
            actionText,
            actionIntentPending
        )
        nb.showNotification(context = context, tag = config.tag, id = config.id)
        return true
    }

    override fun postImageText(
        header: CharSequence,
        body: CharSequence,
        url: String,
        imageSize: NotificationImageSize,
        channel: NotificationChannel,
        config: NotificationConfig,
    ): Boolean {
        if (checkSelfPermission(context, permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
            return false
        }

        context.createNotificationChannel(id = channel.id, context.getString(channel.description))

        fun notify(bitmap: Bitmap) {
            val nb = NotificationCompat.Builder(context, channel.id)
            nb.defaultSettings(context = context, config = config)
            nb.apply(config.builder)
            nb.setContentTitle(header)
            nb.setContentText(body)
            nb.setLargeIcon(bitmap)
            nb.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            nb.showNotification(context = context, tag = config.tag, id = config.id)
        }

        val request = ImageRequest.Builder(context)
            .data(url)
            .size(Size(Dimension(imageSize.width), Dimension(imageSize.height)))
            .listener(
                onSuccess = { request, result ->
                    (result.drawable as? BitmapDrawable)?.bitmap
                        ?.let(::notify)
                        ?: Timber.e("Wrong drawable type")
                },
                onError = { request, result ->  Timber.e(result.throwable) }
            )
            .build()

        imageLoader.enqueue(request)
        return true
    }

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
    ): Boolean {
        if (checkSelfPermission(context, permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
            return false
        }

        context.createNotificationChannel(id = channel.id, context.getString(channel.description))
        val cancelIntentPending = context.getNotificationPendingIntent(actionIntent)
        val nb = NotificationCompat.Builder(context, channel.id)
        nb.defaultSettings(context = context, config = config)
        nb.apply(config.builder)
        nb.setContentTitle(header)
        nb.setOngoing(true)
        nb.addAction(
            actionIcon,
            actionText,
            cancelIntentPending
        )
        nb.setProgress(maxProgress, progress, isIntermediate)
        nb.showNotification(context = context, tag = config.tag, id = config.id)
        return true
    }

}

