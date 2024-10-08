package com.sample.architecturecomponents.core.data.repositories.recent_search

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.common.result.asResult
import com.sample.architecturecomponents.core.data.models.toRecentSearchEntity
import com.sample.architecturecomponents.core.database.dao.RecentSearchDao
import com.sample.architecturecomponents.core.database.model.asExternalModels
import com.sample.architecturecomponents.core.di.IoScope
import com.sample.architecturecomponents.core.model.RecentSearch
import com.sample.architecturecomponents.core.model.RecentSearchTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

internal class RecentSearchRepositoryImpl @Inject constructor(
    private val recentSearchDao: RecentSearchDao,
    @IoScope private val ioScope: CoroutineScope
) : RecentSearchRepository {

    override fun getRecentSearch(query: String, limit: Long, tag: RecentSearchTags): Flow<Result<List<RecentSearch>>> {
        return recentSearchDao.getRecentSearch(query = query, tag = tag, limit = limit)
            .take(1)
            .map { it.asExternalModels() }
            .asResult()
    }

    override suspend fun insert(item: RecentSearch) =
        recentSearchDao.insert(item.toRecentSearchEntity())

    override suspend fun delete(item: RecentSearch) =
        recentSearchDao.delete(item.toRecentSearchEntity())

    override suspend fun delete() =
        recentSearchDao.delete()

}
