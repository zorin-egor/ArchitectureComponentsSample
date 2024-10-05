package com.sample.architecturecomponents.feature.users

import com.sample.architecturecomponents.core.domain.usecases.GetUsersUseCase
import com.sample.architecturecomponents.core.testing.tests.repositories.UsersRepositoryTestImpl
import com.sample.architecturecomponents.core.testing.tests.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
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
    fun loadingUsersViewModelTest() = runTest {
        val items = viewModel.state.first()
        assert(items is UsersUiState.Empty)
    }

}