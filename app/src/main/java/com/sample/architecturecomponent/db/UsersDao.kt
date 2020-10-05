package com.sample.architecturecomponent.db

import androidx.room.*
import com.sample.architecturecomponent.model.User


@Dao
interface UsersDao {

    @Query("SELECT * FROM users ORDER BY id")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM users LIMIT :count OFFSET :from")
    suspend fun getUsersPage(from: Long, count: Int = 30): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<User>)

    @Delete
    suspend fun delete(user: User)

}
