package com.sample.architecturecomponent.db

import androidx.room.*
import com.sample.architecturecomponent.model.Details


@Dao
interface DetailsDao {

    @Query("SELECT * FROM details WHERE user_id = :id")
    suspend fun getDetailsById(id: String): Details

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Details)

    @Delete
    suspend fun delete(item: Details)

}
