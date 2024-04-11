package com.sample.architecturecomponents.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sample.architecturecomponents.core.database.model.DetailsEntity
import com.sample.architecturecomponents.core.database.model.UserAndDetails
import kotlinx.coroutines.flow.Flow


@Dao
interface DetailsDao {

    @Query("SELECT * FROM Details WHERE user_id = :id")
    fun getDetailsById(id: Long): Flow<DetailsEntity?>

    @Transaction
    @Query("SELECT * FROM Users WHERE Users.user_id = :id")
    fun getUserAndDetail(id: Long): Flow<UserAndDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DetailsEntity)

    @Delete
    suspend fun delete(item: DetailsEntity)

}
