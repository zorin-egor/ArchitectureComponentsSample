package com.sample.architecturecomponents.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sample.architecturecomponents.core.database.converters.LicenseConverter
import com.sample.architecturecomponents.core.database.converters.ListStringConverter
import com.sample.architecturecomponents.core.database.dao.DetailsDao
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.DetailsEntity
import com.sample.architecturecomponents.core.database.model.RepositoryEntity
import com.sample.architecturecomponents.core.database.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        DetailsEntity::class,
        RepositoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ListStringConverter::class,
    LicenseConverter::class
)
internal abstract class Database : RoomDatabase() {

    abstract fun detailsDao(): DetailsDao

    abstract fun usersDao(): UsersDao

    abstract fun repositoriesDao(): RepositoriesDao

}
