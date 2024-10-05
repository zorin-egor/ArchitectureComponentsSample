package com.sample.architecturecomponents.feature.repository_details


sealed interface RepositoryDetailsActions {
    data class ShowError(val error: Throwable) : RepositoryDetailsActions
}