package com.sample.architecturecomponents.feature.repository_details

import androidx.lifecycle.SavedStateHandle
import com.sample.architecturecomponents.core.domain.usecases.GetRepositoryDetailsByOwnerUseCase
import com.sample.architecturecomponents.core.testing.tests.repositories.RepositoryDetailsRepositoryTestImpl
import com.sample.architecturecomponents.core.testing.tests.util.MainDispatcherRule
import com.sample.architecturecomponents.core.ui.viewmodels.UiState
import com.sample.architecturecomponents.feature.repository_details.navigation.REPOSITORY_ID
import com.sample.architecturecomponents.feature.repository_details.navigation.REPOSITORY_OWNER
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RepositoryDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = RepositoryDetailsRepositoryTestImpl()

    private val useCase = GetRepositoryDetailsByOwnerUseCase(
        repositoryDetailsRepository = repository,
        dispatcher = mainDispatcherRule.testDispatcher,
    )

    private lateinit var viewModel: RepositoryDetailsViewModel

    @Before
    fun setup() {
        viewModel = RepositoryDetailsViewModel(
            getRepositoryDetailsByIdUseCase = useCase,
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    REPOSITORY_OWNER to "owner",
                    REPOSITORY_ID to "id"
                )
            )
        )
    }

    @Test
    fun getEmptyDataViewModelTest() = runTest(mainDispatcherRule.testDispatcher) {
        val items = viewModel.state.buffer().take(2).toList()
        assertEquals(UiState.Loading, items[0])
        assertEquals(UiState.Empty, items[1])
    }

}