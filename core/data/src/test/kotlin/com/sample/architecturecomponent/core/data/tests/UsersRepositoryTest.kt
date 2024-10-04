package com.sample.architecturecomponent.core.data.tests

import JvmUnitTestDevAssetManager
import com.sample.architecturecomponent.core.data.tests.dao.UsersDaoTestImpl
import com.sample.architecturecomponent.core.data.tests.ext.firstSuccess
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.users.UsersRepository
import com.sample.architecturecomponents.core.data.repositories.users.UsersRepositoryImpl
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.dev.DevNetworkDataSource
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UsersRepositoryTest {

    private val scope = TestScope(TestCoroutineScheduler())
    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private lateinit var subject: UsersRepository
    private lateinit var network: NetworkDataSource
    private lateinit var daoTest: UsersDao

    @Before
    fun setup() {
        daoTest = UsersDaoTestImpl()

        network = DevNetworkDataSource(
            dispatcher = dispatcher,
            networkJson = Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            },
            assets = JvmUnitTestDevAssetManager,
        )

        subject = UsersRepositoryImpl(
            usersDao = daoTest,
            ioScope = scope,
            networkDatasource = network,
        )
    }

    @Test
    fun getUsersEmptyTest() = runTest(dispatcher) {
        val result = subject.getUsers(sinceId = Long.MAX_VALUE).toList()
        assertEquals(2, result.size)
        assertEquals(Result.Loading, result.first())
        assertEquals(Result.Success(emptyList()), result[1])
    }

    @Test
    fun getUsersSinceTest() = runTest(dispatcher) {
        val users = subject.getUsers(sinceId = 1, limit = 2).firstSuccess().map { it.login }
        val expectedNames = listOf("defunkt", "pjhyett")
        assertEquals(expectedNames, users)
    }
    
}