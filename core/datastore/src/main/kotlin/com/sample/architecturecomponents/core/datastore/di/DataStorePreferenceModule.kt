package com.sample.architecturecomponents.core.datastore.di

import android.content.Context
import com.sample.architecturecomponents.core.datastore.DataStorePreference
import com.sample.architecturecomponents.core.datastore.SettingsPreference
import com.sample.architecturecomponents.core.model.AppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStorePreferenceModule {

    @Provides
    @Singleton
    internal fun providesSettingsPreference(
        @ApplicationContext context: Context,
        appConfig: AppConfig
    ): SettingsPreference = DataStorePreference(context, appConfig)

}

