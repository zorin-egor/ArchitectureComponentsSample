package com.sample.architecturecomponents.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.sample.architecturecomponents.core.database.model.RecentSearchEntity
import com.sample.architecturecomponents.core.model.RecentSearchTags
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Query(
        "SELECT * FROM RecentSearch WHERE value LIKE :query || '%' AND tag LIKE :tag " +
        "ORDER BY date DESC LIMIT :limit"
    )
    fun getRecentSearch(query: String, tag: RecentSearchTags, limit: Long = 10): Flow<List<RecentSearchEntity>>

    @Query(
        "SELECT * FROM RecentSearch WHERE value LIKE :query || '%' " +
        "ORDER BY date DESC LIMIT :limit"
    )
    fun getRecentSearch(query: String, limit: Long = 10): Flow<List<RecentSearchEntity>>

    @Upsert
    suspend fun insert(item: RecentSearchEntity)

    @Upsert
    suspend fun insert(items: List<RecentSearchEntity>)

    @Delete
    suspend fun delete(item: RecentSearchEntity)

    @Query("DELETE FROM RecentSearch")
    suspend fun delete()

}
