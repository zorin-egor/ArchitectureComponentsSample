package com.sample.architecturecomponent.di

import com.sample.architecturecomponent.api.Api
import com.sample.architecturecomponent.db.DetailsDao
import com.sample.architecturecomponent.db.UsersDao
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.repositories.details.DetailsRepository
import com.sample.architecturecomponent.repositories.details.DetailsRepositoryImpl
import com.sample.architecturecomponent.repositories.users.UsersRepository
import com.sample.architecturecomponent.repositories.users.UsersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideUsersRepository(retrofit: RetrofitTool<Api>, usersDao: UsersDao): UsersRepository {
        return UsersRepositoryImpl(retrofit, usersDao)
    }

    @Provides
    @Singleton
    fun provideDetailsRepository(retrofit: RetrofitTool<Api>, detailsDao: DetailsDao): DetailsRepository {
        return DetailsRepositoryImpl(retrofit, detailsDao)
    }

}
