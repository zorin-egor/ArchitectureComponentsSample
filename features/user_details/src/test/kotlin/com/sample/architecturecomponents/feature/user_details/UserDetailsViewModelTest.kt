package com.sample.architecturecomponents.feature.user_details

import androidx.lifecycle.SavedStateHandle
import com.sample.architecturecomponents.core.domain.usecases.GetUserDetailsUseCase
import com.sample.architecturecomponents.core.testing.tests.repositories.UserDetailsRepositoryTestImpl
import com.sample.architecturecomponents.core.testing.tests.util.MainDispatcherRule
import com.sample.architecturecomponents.core.ui.viewmodels.UiState
import com.sample.architecturecomponents.feature.user_details.navigation.USER_ID_ARG
import com.sample.architecturecomponents.feature.user_details.navigation.USER_URL_ARG
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
    fun getEmptyUserDetailsViewModelTest() = runTest(mainDispatcherRule.testDispatcher) {
        val items = viewModel.state.buffer().take(2).toList()
        assertEquals(UiState.Loading, items[0])
        assertEquals(UiState.Empty, items[1])
    }

}