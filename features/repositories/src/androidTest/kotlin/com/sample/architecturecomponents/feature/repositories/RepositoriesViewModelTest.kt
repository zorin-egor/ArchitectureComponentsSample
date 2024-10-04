package com.sample.architecturecomponents.feature.repositories

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.sample.architecturecomponents.core.domain.usecases.GetRecentSearchUseCase
import com.sample.architecturecomponents.core.domain.usecases.GetRepositoriesByNameUseCase
import com.sample.architecturecomponents.core.domain.usecases.SetRecentSearchUseCase
import com.sample.architecturecomponents.core.testing.tests.repositories.RecentSearchRepositoryTestImpl
import com.sample.architecturecomponents.core.testing.tests.repositories.RepositoriesRepositoryTestImpl
import com.sample.architecturecomponents.core.testing.tests.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RepositoriesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val reposRepository = RepositoriesRepositoryTestImpl()
    private val recentSearchRepository = RecentSearchRepositoryTestImpl()

    private val repositoriesUseCase = GetRepositoriesByNameUseCase(
        repositoriesRepository = reposRepository,
        dispatcher = mainDispatcherRule.testDispatcher,
        limitPerPage = 5
    )

    private val getRecentSearchUseCase = GetRecentSearchUseCase(
        recentSearchRepository = recentSearchRepository,
        dispatcher = mainDispatcherRule.testDispatcher
    )

    private val setRecentSearchUseCase = SetRecentSearchUseCase(
        recentSearchRepository = recentSearchRepository,
        ioScope = TestScope(mainDispatcherRule.testDispatcher)
    )

    private lateinit var viewModel: RepositoriesViewModel

    @Before
    fun setup() {
        viewModel = RepositoriesViewModel(
            getReposByNameUseCase = repositoriesUseCase,
            context = context,
            getRecentSearchUseCase = getRecentSearchUseCase,
            setRecentSearchUseCase = setRecentSearchUseCase
        )
    }

    @Test
    fun loadingUsersViewModelTest() = runTest {
        val items = viewModel.state.first()
        assertEquals(RepositoriesByNameUiStates.Start, items.state)
    }

}