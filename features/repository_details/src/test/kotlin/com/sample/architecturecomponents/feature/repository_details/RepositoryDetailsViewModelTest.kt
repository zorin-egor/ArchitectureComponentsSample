package com.sample.architecturecomponents.feature.repository_details

import androidx.lifecycle.SavedStateHandle
import com.sample.architecturecomponents.core.domain.usecases.GetRepositoryDetailsByOwnerUseCase
import com.sample.architecturecomponents.core.testing.tests.repositories.RepositoryDetailsRepositoryTestImpl
import com.sample.architecturecomponents.core.testing.tests.util.MainDispatcherRule
import com.sample.architecturecomponents.feature.repository_details.navigation.REPOSITORY_ID
import com.sample.architecturecomponents.feature.repository_details.navigation.REPOSITORY_OWNER
import kotlinx.coroutines.flow.first
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
    fun loadingUsersViewModelTest() = runTest {
        val items = viewModel.state.first()
        assertEquals(RepositoryDetailsUiState.Loading, items)
    }

}