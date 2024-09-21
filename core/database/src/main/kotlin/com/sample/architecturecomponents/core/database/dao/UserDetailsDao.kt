package com.sample.architecturecomponents.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.sample.architecturecomponents.core.database.model.UserAndUserDetails
import com.sample.architecturecomponents.core.database.model.UserDetailsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDetailsDao {

    @Query("SELECT * FROM UserDetails WHERE user_id = :id")
    fun getDetailsById(id: Long): Flow<UserDetailsEntity?>

    @Transaction
    @Query("SELECT * FROM Users WHERE Users.user_id = :id")
    fun getUserAndDetail(id: Long): Flow<UserAndUserDetails>

    @Upsert
    suspend fun insert(item: UserDetailsEntity)

    @Upsert
    suspend fun insert(item: List<UserDetailsEntity>)

    @Delete
    suspend fun delete(item: UserDetailsEntity)

}
