package com.sample.architecturecomponents.app.ui.repositories_details_list_2_pane

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.architecturecomponents.feature.repository_details.navigation.REPOSITORY_ID
import com.sample.architecturecomponents.feature.repository_details.navigation.REPOSITORY_OWNER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class RepositoriesDetailsList2PaneViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val selectedRepository: StateFlow<RepositoryDetailsList2PaneArgs?> =
        savedStateHandle.getStateFlow<String?>(REPOSITORY_OWNER, null)
            .filterNotNull()
            .zip(savedStateHandle.getStateFlow<String?>(REPOSITORY_ID, null)
                .filterNotNull())
            { id, url ->
                RepositoryDetailsList2PaneArgs(
                    userOwner = id,
                    userUrl = url
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = null
            )

    fun onRepositoryClick(repoOwner: String, repoId: String) {
        Timber.d("onRepositoryClick($repoOwner, $repoId)")
        savedStateHandle[REPOSITORY_OWNER] = repoOwner
        savedStateHandle[REPOSITORY_ID] = repoId
    }
}

