package com.sample.architecturecomponent.di

import android.content.Context
import androidx.room.Room
import com.sample.architecturecomponent.db.AppDatabase
import com.sample.architecturecomponent.db.DetailsDao
import com.sample.architecturecomponent.db.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATA_BASE_NAME
        ).build()
    }

    @Provides
    fun provideUsersDao(appDatabase: AppDatabase): UsersDao {
        return appDatabase.usersDao()
    }

    @Provides
    fun provideDetailsDao(appDatabase: AppDatabase): DetailsDao {
        return appDatabase.detailsDao()
    }

}
