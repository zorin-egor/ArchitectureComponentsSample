package com.sample.architecturecomponents.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sample.architecturecomponents.core.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Query("SELECT * FROM Users ORDER BY user_id")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM Users WHERE user_id = :id")
    fun getUserById(id: Long): Flow<UserEntity?>

    @Query("SELECT * FROM Users ORDER BY user_id LIMIT :count OFFSET :from")
    fun getUsersCount(from: Long, count: Long = 30): Flow<List<UserEntity>>

    @Query("SELECT * FROM Users WHERE user_id > :sinceId ORDER BY user_id LIMIT :count")
    fun getUsersSinceId(sinceId: Long, count: Long = 30): Flow<List<UserEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<UserEntity>)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("DELETE FROM Users")
    suspend fun delete()

    @Transaction
    suspend fun clearInsert(items: List<UserEntity>) {
        delete()
        insertAll(items)
    }
}
