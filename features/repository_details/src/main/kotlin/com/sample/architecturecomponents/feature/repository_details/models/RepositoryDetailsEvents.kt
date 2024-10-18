package com.sample.architecturecomponents.feature.repository_details.models


sealed interface RepositoryDetailsEvents {
    data object None : RepositoryDetailsEvents
    data object ShareProfile : RepositoryDetailsEvents
    data object NavigationBack : RepositoryDetailsEvents
}