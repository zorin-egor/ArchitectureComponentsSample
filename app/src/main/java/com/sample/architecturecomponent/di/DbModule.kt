package com.sample.architecturecomponent.di

import android.app.Application
import androidx.room.Room
import com.sample.architecturecomponent.api.Api
import com.sample.architecturecomponent.db.AppDatabase
import com.sample.architecturecomponent.db.DetailsDao
import com.sample.architecturecomponent.db.UsersDao
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.repository.DetailsRepository
import com.sample.architecturecomponent.repository.UsersRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DbModule {

    @Provides
    @Singleton
    fun provideDb(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DATA_BASE_NAME
        ).build()
    }

    @Provides
    fun provideUsersDao(appDatabase: AppDatabase): UsersDao {
        return appDatabase.usersDao()
    }

    @Provides
    @Singleton
    fun provideUsersRepository(retrofit: RetrofitTool<Api>, usersDao: UsersDao): UsersRepository {
        return UsersRepository(retrofit, usersDao)
    }

    @Provides
    fun provideDetailsDao(appDatabase: AppDatabase): DetailsDao {
        return appDatabase.detailsDao()
    }

    @Provides
    @Singleton
    fun provideDetailsRepository(retrofit: RetrofitTool<Api>, detailsDao: DetailsDao): DetailsRepository {
        return DetailsRepository(retrofit, detailsDao)
    }

}
