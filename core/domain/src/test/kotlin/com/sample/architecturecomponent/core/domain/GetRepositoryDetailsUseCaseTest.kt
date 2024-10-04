package com.sample.architecturecomponent.core.domain

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetRepositoryDetailsByOwnerUseCase
import com.sample.architecturecomponents.core.testing.tests.repositories.RepositoryDetailsRepositoryTestImpl
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetRepositoryDetailsUseCaseTest {

    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private val repository = RepositoryDetailsRepositoryTestImpl()

    private val userCase = GetRepositoryDetailsByOwnerUseCase(
        repositoryDetailsRepository = repository,
        dispatcher = dispatcher,
    )

    @Test
    fun getRepositoryDetailsEmptyData() = runTest(dispatcher) {
        val items = userCase(owner = "", repo = "").toList()
        assertEquals(2, items.size)
        assertEquals(Result.Loading, items.first())
        assert(items[1] is Result.Error)
    }

}