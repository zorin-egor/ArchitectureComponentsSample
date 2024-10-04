package com.sample.architecturecomponents.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
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

    @Query("SELECT * FROM Users WHERE user_id > :sinceId ORDER BY user_id LIMIT :limit")
    fun getUsersSinceId(sinceId: Long, limit: Long = 30): Flow<List<UserEntity>>

    @Query("""
        SELECT * FROM
            (SELECT * FROM Users WHERE user_id < :sinceId ORDER BY user_id)
        UNION
        SELECT * FROM
            (SELECT * FROM Users WHERE user_id >= :sinceId ORDER BY user_id LIMIT :limit)
    """)
    fun getUsersUntilSinceId(sinceId: Long, limit: Long = 30): Flow<List<UserEntity>>

    @Upsert
    suspend fun insert(item: UserEntity)

    @Upsert
    suspend fun insert(items: List<UserEntity>)

    @Delete
    suspend fun delete(item: UserEntity)

    @Query("DELETE FROM Users")
    suspend fun delete()

    @Transaction
    suspend fun clearInsert(items: List<UserEntity>) {
        delete()
        insert(items)
    }
}
