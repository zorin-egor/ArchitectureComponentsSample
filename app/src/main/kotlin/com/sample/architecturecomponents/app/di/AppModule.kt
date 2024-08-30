package com.sample.architecturecomponents.app.di

import com.sample.architecturecomponents.app.BuildConfig
import com.sample.architecturecomponents.app.MainActivity
import com.sample.architecturecomponents.core.common.di.MainScreenClass
import com.sample.architecturecomponents.core.model.AppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesConfig(): AppConfig = AppConfig(
        appVersion = BuildConfig.VERSION_NAME,
        appCode = BuildConfig.VERSION_CODE,
        baseUrl = BuildConfig.BACKEND_URL
    )

    @MainScreenClass
    @Provides
    fun provideMainClass(): Class<*> = MainActivity::class.java
}
