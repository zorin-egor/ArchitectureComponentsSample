package com.sample.architecturecomponents.core.data.repositories.recent_search

import com.sample.architecturecomponents.core.model.RecentSearch
import com.sample.architecturecomponents.core.model.RecentSearchTags
import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {

    fun getRecentSearch(
        query: String,
        limit: Long = 10,
        tag: RecentSearchTags = RecentSearchTags.None
    ): Flow<List<RecentSearch>>

    suspend fun insert(item: RecentSearch)

    suspend fun delete(item: RecentSearch)

    suspend fun delete()

}