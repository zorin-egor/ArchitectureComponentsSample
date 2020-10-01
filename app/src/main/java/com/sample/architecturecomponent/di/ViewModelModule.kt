package com.sample.architecturecomponent.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModelFactory
import com.sample.architecturecomponent.ui.fragments.details.DetailsViewModel
import com.sample.architecturecomponent.ui.fragments.splash.SplashViewModel
import com.sample.architecturecomponent.ui.fragments.users.UsersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UsersViewModel::class)
    abstract fun bindUsersModel(userViewModel: UsersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsModel(detailsViewModel: DetailsViewModel): ViewModel

}
