package com.sample.architecturecomponents.core.data.di

import com.sample.architecturecomponents.core.data.repositories.details.DetailsRepository
import com.sample.architecturecomponents.core.data.repositories.details.DetailsRepositoryImpl
import com.sample.architecturecomponents.core.data.repositories.recent_search.RecentSearchRepository
import com.sample.architecturecomponents.core.data.repositories.recent_search.RecentSearchRepositoryImpl
import com.sample.architecturecomponents.core.data.repositories.repositories.RepositoriesRepository
import com.sample.architecturecomponents.core.data.repositories.repositories.RepositoriesRepositoryImpl
import com.sample.architecturecomponents.core.data.repositories.settings.SettingsDataRepository
import com.sample.architecturecomponents.core.data.repositories.settings.SettingsDataRepositoryImpl
import com.sample.architecturecomponents.core.data.repositories.users.UsersRepository
import com.sample.architecturecomponents.core.data.repositories.users.UsersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    internal abstract fun bindsUsersRepository(
        usersRepository: UsersRepositoryImpl,
    ): UsersRepository

    @Binds
    internal abstract fun bindsDetailsRepository(
        detailsRepository: DetailsRepositoryImpl,
    ): DetailsRepository

    @Binds
    internal abstract fun bindsSettingsDataRepository(
        settingsDataRepository: SettingsDataRepositoryImpl,
    ): SettingsDataRepository

    @Binds
    internal abstract fun bindsReposRepository(
        repository: RepositoriesRepositoryImpl,
    ): RepositoriesRepository

    @Binds
    internal abstract fun bindsRecentSearchRepository(
        repository: RecentSearchRepositoryImpl,
    ): RecentSearchRepository

}
