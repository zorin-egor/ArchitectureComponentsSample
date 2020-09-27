package com.sample.architecturecomponent.di

import com.sample.architecturecomponent.ui.fragments.splash.SplashFragment
import com.sample.architecturecomponent.ui.fragments.users.UsersFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun contributeUsersFragment(): UsersFragment

}
