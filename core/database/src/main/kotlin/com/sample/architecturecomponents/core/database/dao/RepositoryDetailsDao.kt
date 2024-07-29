package com.sample.architecturecomponents.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.architecturecomponents.core.database.model.RepositoryDetailsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RepositoryDetailsDao {

    @Query("SELECT * FROM Repositories_details WHERE owner = :owner AND name = :name")
    fun getDetailsByOwnerAndName(owner: String, name: String): Flow<RepositoryDetailsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RepositoryDetailsEntity)

    @Delete
    suspend fun delete(item: RepositoryDetailsEntity)

}
