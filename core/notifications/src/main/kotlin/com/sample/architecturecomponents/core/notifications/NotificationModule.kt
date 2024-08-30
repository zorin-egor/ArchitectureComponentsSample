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
    fun providesNotification(@ApplicationContext context: Context, imageLoader: ImageLoader): Notifier =
        NotifierImpl(context, imageLoader)
}
