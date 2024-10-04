package com.sample.architecturecomponent.core.data.tests.dao

import com.sample.architecturecomponents.core.database.dao.RecentSearchDao
import com.sample.architecturecomponents.core.database.model.RecentSearchEntity
import com.sample.architecturecomponents.core.model.RecentSearchTags
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class RecentSearchDaoTestImpl : RecentSearchDao {

    private val dbStateFlow = MutableStateFlow(emptyList<RecentSearchEntity>())

    override fun getRecentSearch(query: String, tag: RecentSearchTags, limit: Long): Flow<List<RecentSearchEntity>> =
        dbStateFlow.map { flow ->
            flow.filter { it.value.startsWith(query) && it.tag == tag }
                .take(limit.toInt())
        }

    override fun getRecentSearch(query: String, limit: Long): Flow<List<RecentSearchEntity>> =
        dbStateFlow.map { flow ->
            flow.filter { it.value.startsWith(query) }.take(limit.toInt())
        }

    override suspend fun insert(item: RecentSearchEntity) =
        dbStateFlow.update { (it + item).distinctBy(RecentSearchEntity::value) }

    override suspend fun insert(items: List<RecentSearchEntity>) =
        dbStateFlow.update { (it + items).distinctBy(RecentSearchEntity::value) }

    override suspend fun delete(item: RecentSearchEntity) =
        dbStateFlow.update { flow -> flow.toMutableList().apply { remove(item) } }

    override suspend fun delete() =
        dbStateFlow.update { flow -> flow.toMutableList().apply { clear() } }
}