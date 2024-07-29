package com.sample.architecturecomponents.core.data.di

import com.sample.architecturecomponents.core.data.repositories.recent_search.RecentSearchRepository
import com.sample.architecturecomponents.core.data.repositories.recent_search.RecentSearchRepositoryImpl
import com.sample.architecturecomponents.core.data.repositories.repositories.RepositoriesRepository
import com.sample.architecturecomponents.core.data.repositories.repositories.RepositoriesRepositoryImpl
import com.sample.architecturecomponents.core.data.repositories.repository_details.RepositoryDetailsRepository
import com.sample.architecturecomponents.core.data.repositories.repository_details.RepositoryDetailsRepositoryImpl
import com.sample.architecturecomponents.core.data.repositories.settings.SettingsRepository
import com.sample.architecturecomponents.core.data.repositories.settings.SettingsRepositoryImpl
import com.sample.architecturecomponents.core.data.repositories.theme.ThemeRepository
import com.sample.architecturecomponents.core.data.repositories.theme.ThemeRepositoryImpl
import com.sample.architecturecomponents.core.data.repositories.user_details.UserDetailsRepository
import com.sample.architecturecomponents.core.data.repositories.user_details.UserDetailsRepositoryImpl
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
        detailsRepository: UserDetailsRepositoryImpl,
    ): UserDetailsRepository

    @Binds
    internal abstract fun bindsThemeDataRepository(
        themeDataRepository: ThemeRepositoryImpl,
    ): ThemeRepository

    @Binds
    internal abstract fun bindsSettingsDataRepository(
        settingsDataRepository: SettingsRepositoryImpl,
    ): SettingsRepository

    @Binds
    internal abstract fun bindsReposRepository(
        repository: RepositoriesRepositoryImpl,
    ): RepositoriesRepository

    @Binds
    internal abstract fun bindsRepoDetailsRepository(
        repository: RepositoryDetailsRepositoryImpl,
    ): RepositoryDetailsRepository

    @Binds
    internal abstract fun bindsRecentSearchRepository(
        repository: RecentSearchRepositoryImpl,
    ): RecentSearchRepository

}
