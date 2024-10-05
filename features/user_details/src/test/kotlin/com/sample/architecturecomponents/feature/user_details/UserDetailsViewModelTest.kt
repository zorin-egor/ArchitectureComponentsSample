package com.sample.architecturecomponents.feature.user_details

import androidx.lifecycle.SavedStateHandle
import com.sample.architecturecomponents.core.domain.usecases.GetUserDetailsUseCase
import com.sample.architecturecomponents.core.testing.tests.repositories.UserDetailsRepositoryTestImpl
import com.sample.architecturecomponents.core.testing.tests.util.MainDispatcherRule
import com.sample.architecturecomponents.feature.user_details.navigation.USER_ID_ARG
import com.sample.architecturecomponents.feature.user_details.navigation.USER_URL_ARG
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class UserDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = UserDetailsRepositoryTestImpl()

    private val useCase = GetUserDetailsUseCase(
        userDetailsRepository = repository,
        dispatcher = mainDispatcherRule.testDispatcher,
    )

    private lateinit var viewModel: UserDetailsViewModel

    @Before
    fun setup() {
        viewModel = UserDetailsViewModel(
            getUserDetailsUseCase = useCase,
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    USER_URL_ARG to "url",
                    USER_ID_ARG to 0
                )
            )
        )
    }

    @Test
    fun loadingUsersViewModelTest() = runTest {
        val items = viewModel.state.first()
        assertEquals(UserDetailsUiState.Loading, items)
    }

}