package com.sample.architecturecomponents.feature.details


sealed interface DetailsActions {
    data class ShowError(val error: String) : DetailsActions
}