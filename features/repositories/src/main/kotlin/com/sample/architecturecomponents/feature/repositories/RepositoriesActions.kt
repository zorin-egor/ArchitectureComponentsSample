package com.sample.architecturecomponents.feature.repositories


sealed interface RepositoriesActions {
    data class ShowError(val error: Throwable) : RepositoriesActions
}