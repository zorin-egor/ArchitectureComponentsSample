package com.sample.architecturecomponent.di

import android.app.Application
import com.sample.architecturecomponent.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivitiesModule::class
])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)

}