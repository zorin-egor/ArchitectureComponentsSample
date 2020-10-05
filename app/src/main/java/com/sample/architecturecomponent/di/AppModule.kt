package com.sample.architecturecomponent.di

import android.app.Application
import android.content.Context
import com.sample.architecturecomponent.api.Api
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    ViewModelModule::class,
    DbModule::class
])
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideRetrofitTool(context: Context): RetrofitTool<Api> {
        return RetrofitTool(context, Api.BASE_URL, Api::class.java)
    }

}
