package com.sample.architecturecomponents.feature.users

import com.sample.architecturecomponents.core.domain.usecases.GetUsersUseCase
import com.sample.architecturecomponents.core.testing.tests.repositories.UsersRepositoryTestImpl
import com.sample.architecturecomponents.core.testing.tests.util.MainDispatcherRule
import com.sample.architecturecomponents.core.ui.viewmodels.UiState
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = UsersRepositoryTestImpl()

    private val useCase = GetUsersUseCase(
        usersRepository = repository,
        dispatcher = mainDispatcherRule.testDispatcher,
        limitPerPage = 5
    )

    private lateinit var viewModel: UsersViewModel

    @Before
    fun setup() {
        viewModel = UsersViewModel(
            getSearchContentsUseCase = useCase,
        )
    }

    @Test
    fun getEmptyDataViewModelTest() = runTest(mainDispatcherRule.testDispatcher) {
        val items = viewModel.state.buffer().take(2).toList()
        assert(items[0] is UiState.Loading)
        assert(items[1] is UiState.Empty)
    }

}