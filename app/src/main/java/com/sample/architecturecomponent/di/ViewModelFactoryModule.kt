package com.sample.architecturecomponent.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.architecturecomponent.ui.fragments.base.BaseViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        ViewModelModule::class
    ]
)
class ViewModelFactoryModule {

    @Provides
    @Singleton
    fun provideViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory {
        return BaseViewModelFactory(creators)
    }

}