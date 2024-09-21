package com.sample.architecturecomponents.core.database.di

import com.sample.architecturecomponents.core.database.AppDatabase
import com.sample.architecturecomponents.core.database.dao.RecentSearchDao
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.dao.RepositoryDetailsDao
import com.sample.architecturecomponents.core.database.dao.UserDetailsDao
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
        database: AppDatabase,
    ): UsersDao = database.usersDao()

    @Provides
    fun providesDetailsDao(
        database: AppDatabase,
    ): UserDetailsDao = database.userDetailsDao()

    @Provides
    fun providesReposDao(
        database: AppDatabase,
    ): RepositoriesDao = database.repositoriesDao()

    @Provides
    fun providesRepoDetailsDao(
        database: AppDatabase,
    ): RepositoryDetailsDao = database.repositoryDetailsDao()

    @Provides
    fun providesRecentSearchDao(
        database: AppDatabase,
    ): RecentSearchDao = database.recentSearchDao()
}
