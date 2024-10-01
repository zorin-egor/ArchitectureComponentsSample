package com.sample.architecturecomponents.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.sample.architecturecomponents.core.database.model.UserDetailsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDetailsDao {

    @Query("SELECT * FROM UserDetails WHERE user_id = :id")
    fun getDetailsById(id: Long): Flow<UserDetailsEntity?>

    @Upsert
    suspend fun insert(item: UserDetailsEntity)

    @Upsert
    suspend fun insert(items: List<UserDetailsEntity>)

    @Delete
    suspend fun delete(item: UserDetailsEntity)

}
