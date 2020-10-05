package com.sample.architecturecomponent.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.architecturecomponent.model.Details


@Dao
interface DetailsDao {

    @Query("SELECT * FROM details WHERE id = :id")
    suspend fun getDetailsById(id: String): Details

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(details: Details)

}
