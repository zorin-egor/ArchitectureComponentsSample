package com.sample.architecturecomponents.core.domain.usecases

import com.sample.architecturecomponents.core.data.repositories.recent_search.RecentSearchRepository
import com.sample.architecturecomponents.core.model.RecentSearch
import com.sample.architecturecomponents.core.model.RecentSearchTags
import com.sample.architecturecomponents.core.network.di.IoScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class SetRecentSearchUseCase @Inject constructor(
    private val recentSearchRepository: RecentSearchRepository,
    @IoScope private val ioScope: CoroutineScope
) {

    operator fun invoke(query: String, tag: RecentSearchTags = RecentSearchTags.None) =
        ioScope.launch {
            recentSearchRepository.insert(RecentSearch(value = query, tag = tag))
        }
}
