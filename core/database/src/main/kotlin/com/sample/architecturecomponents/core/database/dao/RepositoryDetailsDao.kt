package com.sample.architecturecomponents.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.sample.architecturecomponents.core.database.model.RepositoryDetailsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RepositoryDetailsDao {

    @Query("SELECT * FROM Repositories_details WHERE owner = :owner AND name = :name")
    fun getDetailsByOwnerAndName(owner: String, name: String): Flow<RepositoryDetailsEntity?>

    @Upsert
    suspend fun insert(item: RepositoryDetailsEntity)

    @Upsert
    suspend fun insert(items: List<RepositoryDetailsEntity>)

    @Delete
    suspend fun delete(item: RepositoryDetailsEntity)

}
