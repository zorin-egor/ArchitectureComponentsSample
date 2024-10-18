package com.sample.architecturecomponent.core.domain

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetRepositoriesByNameUseCase
import com.sample.architecturecomponents.core.model.Repository
import com.sample.architecturecomponents.core.testing.tests.repositories.RepositoriesRepositoryTestImpl
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Test
import kotlin.test.assertEquals

class GetRepositoriesByNameUseCaseTest {

    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private val repository = RepositoriesRepositoryTestImpl()

    private val userCase = GetRepositoriesByNameUseCase(
        repositoriesRepository = repository,
        dispatcher = dispatcher,
        limitPerPage = 5
    )

    @Test
    fun getRepositoriesByNameLoading() = runTest(dispatcher) {
        val result = userCase(name = "value").buffer().take(2).toList()
        assert(result.size == 2)
        assertEquals(Result.Loading, result[0])
        assertEquals(emptyList(), (result[1] as Result.Success<List<Repository>>).data)
    }

    @Test
    fun getRepositoriesByNamePaging() = runTest(dispatcher) {
        repeat(10) {
            repository.insert(
                getRepository(
                    id = (it + 1).toLong(),
                    userId = (it + 1).toLong(),
                    owner = "owner1$it",
                    name = "name1$it"
                )
            )
        }

        repeat(10) {
            repository.insert(
                getRepository(
                    id = (it + 1).toLong() * 100,
                    userId = (it + 1).toLong() * 100,
                    owner = "owner2$it",
                    name = "name2$it"
                )
            )
        }

        val first = userCase(name = "name1").buffer().take(2).toList()[1] as Result.Success
        val second = userCase().buffer().take(2).toList()[1] as Result.Success
        val third = userCase().buffer().take(2).toList()[1] as Result.Success

        assert(first.data.size + second.data.size + third.data.size == 25)
    }
}

private fun getRepository(
    id: Long,
    userId: Long,
    owner: String,
    name: String,
    createdAt: Long = 0,
    updatedAt: Long = 0,
) = Repository(
    id = id,
    userId = userId,
    owner = owner,
    avatarUrl = null,
    name = name,
    forks = 0,
    watchersCount = 0,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    updatedAt = Instant.fromEpochMilliseconds(updatedAt),
    stargazersCount = 0,
    description = null
)
