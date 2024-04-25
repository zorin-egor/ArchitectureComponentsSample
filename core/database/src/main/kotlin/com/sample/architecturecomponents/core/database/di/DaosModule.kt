package com.sample.architecturecomponents.core.database.di

import com.sample.architecturecomponents.core.database.Database
import com.sample.architecturecomponents.core.database.dao.DetailsDao
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.dao.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesUsersDao(
        database: Database,
    ): UsersDao = database.usersDao()

    @Provides
    fun providesDetailsDao(
        database: Database,
    ): DetailsDao = database.detailsDao()

    @Provides
    fun providesReposDao(
        database: Database,
    ): RepositoriesDao = database.repositoriesDao()
}
