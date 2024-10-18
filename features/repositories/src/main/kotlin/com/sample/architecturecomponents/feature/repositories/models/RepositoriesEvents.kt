package com.sample.architecturecomponents.feature.repositories.models

import com.sample.architecturecomponents.core.model.Repository


sealed interface RepositoriesEvents {
    data object None : RepositoriesEvents
    data class OnRepositoryClick(val item: Repository): RepositoriesEvents
    data object NextRepositories : RepositoriesEvents
    data class SearchQuery(val query: String) : RepositoriesEvents
}