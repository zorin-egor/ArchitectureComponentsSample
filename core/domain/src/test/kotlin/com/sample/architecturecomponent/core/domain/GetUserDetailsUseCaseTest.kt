package com.sample.architecturecomponent.core.domain

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetUserDetailsUseCase
import com.sample.architecturecomponents.core.testing.tests.repositories.UserDetailsRepositoryTestImpl
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetUserDetailsUseCaseTest {

    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private val repository = UserDetailsRepositoryTestImpl()

    private val userCase = GetUserDetailsUseCase(
        userDetailsRepository = repository,
        dispatcher = dispatcher,
    )

    @Test
    fun getUserDetailsEmptyData() = runTest(dispatcher) {
        val item = userCase(userId = 0, url = "url").take(2).toList()
        assertEquals(2, item.size)
        assertEquals(Result.Loading, item.first())
        assert(item[1] is Result.Error)
    }

}