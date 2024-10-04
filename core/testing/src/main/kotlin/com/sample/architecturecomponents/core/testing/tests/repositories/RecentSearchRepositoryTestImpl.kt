package com.sample.architecturecomponents.core.testing.tests.repositories

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.recent_search.RecentSearchRepository
import com.sample.architecturecomponents.core.model.RecentSearch
import com.sample.architecturecomponents.core.model.RecentSearchTags
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class RecentSearchRepositoryTestImpl : RecentSearchRepository {

    private val recentSearch = mutableListOf<RecentSearch>()

    override fun getRecentSearch(query: String, limit: Long, tag: RecentSearchTags): Flow<Result<List<RecentSearch>>> =
        flowOf(recentSearch.filter { it.value.startsWith(query) && it.tag == tag }
            .take(limit.toInt())
            .let { Result.Success(it) })

    override suspend fun insert(item: RecentSearch) {
        val index = recentSearch.indexOfFirst { it.value == item.value && it.tag == item.tag }
        if (index >= 0) {
            recentSearch[index] = item
        } else {
            recentSearch.add(item)
        }
    }

    override suspend fun delete(item: RecentSearch) {
        recentSearch.removeIf { it.value == item.value && it.tag == item.tag  }
    }

    override suspend fun delete() {
        recentSearch.clear()
    }


}