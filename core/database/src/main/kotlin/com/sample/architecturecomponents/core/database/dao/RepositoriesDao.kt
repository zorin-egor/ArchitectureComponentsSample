package com.sample.architecturecomponents.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.sample.architecturecomponents.core.database.model.RepositoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoriesDao {

    @Query("SELECT * FROM Repositories ORDER BY name")
    fun getRepos(): Flow<List<RepositoryEntity>>

    @Query(
        "SELECT * FROM Repositories WHERE name LIKE '%' || :name || '%' OR " +
        "description LIKE '%' || :name || '%' ORDER BY name ASC LIMIT :limit OFFSET :offset"
    )
    fun getRepositoriesByName(name: String, offset: Long = 0, limit: Long = 30): Flow<List<RepositoryEntity>>

    @Upsert
    suspend fun insert(item: RepositoryEntity)

    @Upsert
    suspend fun insert(items: List<RepositoryEntity>)

    @Delete
    suspend fun delete(item: RepositoryEntity)

    @Query("DELETE FROM Repositories")
    suspend fun delete()

    @Transaction
    suspend fun clearInsert(items: List<RepositoryEntity>) {
        delete()
        insert(items)
    }
}
