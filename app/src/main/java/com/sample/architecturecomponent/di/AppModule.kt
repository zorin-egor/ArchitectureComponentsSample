package com.sample.architecturecomponent.di

import android.content.Context
import com.sample.architecturecomponent.BuildConfig
import com.sample.architecturecomponent.api.Api
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.managers.tools.retrofit.ConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideRetrofitTool(@ApplicationContext context: Context): RetrofitTool<Api> {
        return RetrofitTool(Api.BASE_URL, Api::class.java).apply {
            okHttpBuilder.addInterceptor(ConnectionInterceptor(context))
            if (BuildConfig.DEBUG) {
                okHttpBuilder.addInterceptor(HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BASIC)
                )
            }
        }
    }

}
