package com.sample.architecturecomponents.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sample.architecturecomponents.core.database.converters.InstantConverter
import com.sample.architecturecomponents.core.database.converters.LicenseConverter
import com.sample.architecturecomponents.core.database.converters.ListStringConverter
import com.sample.architecturecomponents.core.database.converters.RecentSearchTagsConverter
import com.sample.architecturecomponents.core.database.dao.RecentSearchDao
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.dao.RepositoryDetailsDao
import com.sample.architecturecomponents.core.database.dao.UserDetailsDao
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.RecentSearchEntity
import com.sample.architecturecomponents.core.database.model.RepositoryDetailsEntity
import com.sample.architecturecomponents.core.database.model.RepositoryEntity
import com.sample.architecturecomponents.core.database.model.UserDetailsEntity
import com.sample.architecturecomponents.core.database.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        UserDetailsEntity::class,
        RepositoryEntity::class,
        RepositoryDetailsEntity::class,
        RecentSearchEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ListStringConverter::class,
    LicenseConverter::class,
    InstantConverter::class,
    RecentSearchTagsConverter::class
)
internal abstract class Database : RoomDatabase() {

    abstract fun detailsDao(): UserDetailsDao

    abstract fun usersDao(): UsersDao

    abstract fun repositoriesDao(): RepositoriesDao

    abstract fun repositoryDetailsDao(): RepositoryDetailsDao

    abstract fun recentSearchDao(): RecentSearchDao

}
