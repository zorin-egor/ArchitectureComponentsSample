package com.sample.architecturecomponents.core.notifications

import android.content.Context
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    fun providesTextNotification(@ApplicationContext context: Context, imageLoader: ImageLoader): NotificationActions =
        NotificationActionsImpl(context, imageLoader)

    @Provides
    fun providesTextNotificationRes(@ApplicationContext context: Context, textNotification: NotificationActions): NotificationActionsRes =
        NotificationActionsResImpl(context, textNotification)
}
