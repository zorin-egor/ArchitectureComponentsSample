package com.sample.architecturecomponent.db

import androidx.room.*
import com.sample.architecturecomponent.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Query("SELECT * FROM users ORDER BY user_id")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM users ORDER BY user_id LIMIT :count OFFSET :from")
    fun getUsers(from: Int, count: Int = 30): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<User>)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM users")
    suspend fun delete()

    @Transaction
    suspend fun clearInsert(items: List<User>) {
        delete()
        insertAll(items)
    }
}
