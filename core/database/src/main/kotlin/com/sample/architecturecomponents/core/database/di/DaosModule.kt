package com.sample.architecturecomponents.core.database.di

import com.sample.architecturecomponents.core.database.Database
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
        database: Database,
    ): UsersDao = database.usersDao()

    @Provides
    fun providesDetailsDao(
        database: Database,
    ): UserDetailsDao = database.detailsDao()

    @Provides
    fun providesReposDao(
        database: Database,
    ): RepositoriesDao = database.repositoriesDao()

    @Provides
    fun providesRepoDetailsDao(
        database: Database,
    ): RepositoryDetailsDao = database.repositoryDetailsDao()

    @Provides
    fun providesRecentSearchDao(
        database: Database,
    ): RecentSearchDao = database.recentSearchDao()
}
