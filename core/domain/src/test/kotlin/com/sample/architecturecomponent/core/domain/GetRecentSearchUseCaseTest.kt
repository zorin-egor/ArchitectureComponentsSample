package com.sample.architecturecomponent.core.domain

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetRecentSearchUseCase
import com.sample.architecturecomponents.core.model.RecentSearch
import com.sample.architecturecomponents.core.model.RecentSearchTags
import com.sample.architecturecomponents.core.testing.tests.repositories.RecentSearchRepositoryTestImpl
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Test
import kotlin.test.assertEquals

class GetRecentSearchUseCaseTest {

    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private val repository = RecentSearchRepositoryTestImpl()

    private val userCase = GetRecentSearchUseCase(
        recentSearchRepository = repository,
        dispatcher = dispatcher
    )

    @Test
    fun getRecentSearchEmptyData() = runTest(dispatcher) {
        val result = userCase(query = "value").toList()
        assert(result.size == 1)
        assertEquals(emptyList(), (result.first() as Result.Success<List<RecentSearch>>).data)
    }

    @Test
    fun getRecentSearchValues() = runTest(dispatcher) {
        repository.insert(
            RecentSearch(
                value = "value",
                date = Instant.fromEpochMilliseconds(0.toLong()),
                tag = RecentSearchTags.Users
            )
        )

        repeat(10) {
            repository.insert(
                RecentSearch(
                    value = "value$it",
                    date = Instant.fromEpochMilliseconds(it.toLong()),
                    tag = RecentSearchTags.Users
                )
            )
        }

        val results = userCase(query = "value", tag = RecentSearchTags.Users).toList()
        assert(results.size == 1)

        val result = results.first() as Result.Success
        assert(result.data.size == 4)
    }

}