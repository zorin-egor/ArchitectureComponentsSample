package com.sample.architecturecomponent.core.domain

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.domain.usecases.GetUsersUseCase
import com.sample.architecturecomponents.core.model.User
import com.sample.architecturecomponents.core.testing.tests.repositories.UsersRepositoryTestImpl
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetUsersUseCaseTest {

    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private val repository = UsersRepositoryTestImpl()

    private val userCase = GetUsersUseCase(
        usersRepository = repository,
        dispatcher = dispatcher,
        limitPerPage = 5
    )

    @Test
    fun getUsersEmptyData() = runTest(dispatcher) {
        val result = userCase(id = 0).toList()
        assert(result.size == 2)
        assertEquals(emptyList(), (result[1] as Result.Success<List<User>>).data)
    }

    @Test
    fun getUsersSinceIdPaging() = runTest(dispatcher) {
        repeat(10) {
            repository.insert(
                getUser(
                    id = it.toLong(),
                    nodeId = it.toString(),
                    login = "login1$it",
                )
            )
        }

        repeat(10) {
            repository.insert(
                getUser(
                    id = (it + 1).toLong() * 10,
                    nodeId = ((it + 1) * 10).toString(),
                    login = "login2$it",
                )
            )
        }

        val first = userCase(id = 4).toList()[1] as Result.Success
        val other = (0..2).map { userCase().toList()[1] as Result.Success }
        val otherTotal = other.sumOf { it.data.size }

        assertEquals(45, first.data.size + otherTotal)

        val usersSet = buildSet<User> {
            addAll(first.data)
            other.forEach {
                addAll(it.data)
            }
        }

        assertEquals(15, usersSet.size)
    }
}

fun getUser(
    id: Long,
    nodeId: String,
    login: String,
) = User(
    id = id,
    nodeId = nodeId,
    login = login,
    url = "url",
    avatarUrl = null,
)