package com.sample.architecturecomponent.di

import com.sample.architecturecomponent.ui.activities.MainActivity
import com.sample.architecturecomponent.ui.activities.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    abstract fun contributeMainActivity(): MainActivity

}
