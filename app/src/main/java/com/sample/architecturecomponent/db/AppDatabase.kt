package com.sample.architecturecomponent.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.architecturecomponent.models.Details
import com.sample.architecturecomponent.models.User

@Database(
    entities = [
        User::class,
        Details::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        val DATA_BASE_NAME = "${AppDatabase::class.java.simpleName}.db"
    }

    abstract fun detailsDao(): DetailsDao

    abstract fun usersDao(): UsersDao

}
