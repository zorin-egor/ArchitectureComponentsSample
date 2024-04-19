package com.sample.architecturecomponents.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.architecturecomponents.core.database.dao.DetailsDao
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.DetailsEntity
import com.sample.architecturecomponents.core.database.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        DetailsEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class Database : RoomDatabase() {

    abstract fun detailsDao(): DetailsDao

    abstract fun usersDao(): UsersDao

}
