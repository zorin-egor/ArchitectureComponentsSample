package com.sample.architecturecomponents.core.domain.usecases

import com.sample.architecturecomponents.core.data.repositories.recent_search.RecentSearchRepository
import com.sample.architecturecomponents.core.model.RecentSearch
import com.sample.architecturecomponents.core.model.RecentSearchTags
import com.sample.architecturecomponents.core.network.Dispatcher
import com.sample.architecturecomponents.core.network.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetRecentSearchUseCase @Inject constructor(
    private val recentSearchRepository: RecentSearchRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {

    companion object {
        private const val LIMIT = 5L
    }

    operator fun invoke(query: String, tag: RecentSearchTags = RecentSearchTags.None): Flow<List<RecentSearch>> =
        recentSearchRepository.getRecentSearch(query = query, limit = LIMIT, tag = tag)
            .map { tags -> tags.filter { it.value != query  } }
            .flowOn(dispatcher)
}
