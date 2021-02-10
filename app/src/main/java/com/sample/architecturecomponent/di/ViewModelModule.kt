package com.sample.architecturecomponent.di

import androidx.lifecycle.ViewModel
import com.sample.architecturecomponent.ui.fragments.details.DetailsViewModel
import com.sample.architecturecomponent.ui.fragments.splash.SplashViewModel
import com.sample.architecturecomponent.ui.fragments.users.UsersViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.multibindings.IntoMap

@InstallIn(FragmentComponent::class)
@Module
abstract class ViewModelModule {

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
