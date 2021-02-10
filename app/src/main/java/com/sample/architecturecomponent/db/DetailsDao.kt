package com.sample.architecturecomponent.db

import androidx.room.*
import com.sample.architecturecomponent.models.Details
import kotlinx.coroutines.flow.Flow


@Dao
interface DetailsDao {

    @Query("SELECT * FROM details WHERE user_id = :id")
    fun getDetailsById(id: Long): Flow<Details?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Details)

    @Delete
    suspend fun delete(item: Details)

}
