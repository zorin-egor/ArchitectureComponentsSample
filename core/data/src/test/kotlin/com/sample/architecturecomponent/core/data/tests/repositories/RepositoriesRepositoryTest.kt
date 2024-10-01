package com.sample.architecturecomponent.core.data.tests.repositories

import JvmUnitTestDevAssetManager
import com.sample.architecturecomponent.core.data.tests.dao.RepositoriesDaoTest
import com.sample.architecturecomponent.core.data.tests.ext.firstSuccess
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.repositories.RepositoriesRepository
import com.sample.architecturecomponents.core.data.repositories.repositories.RepositoriesRepositoryImpl
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
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

class RepositoriesRepositoryTest {

    private val scope = TestScope(TestCoroutineScheduler())
    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private lateinit var subject: RepositoriesRepository
    private lateinit var network: NetworkDataSource
    private lateinit var repositoriesDaoTest: RepositoriesDao

    @Before
    fun setup() {
        repositoriesDaoTest = RepositoriesDaoTest()
        network = DevNetworkDataSource(
            dispatcher = dispatcher,
            networkJson = Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            },
            assets = JvmUnitTestDevAssetManager,
        )
        subject = RepositoriesRepositoryImpl(
            repositoriesDao = repositoriesDaoTest,
            ioScope = scope,
            networkDatasource = network
        )
    }

    @Test
    fun repositoryEmptyTest() = runTest(dispatcher) {
        val result = subject.getRepositoriesByName(name = "value", page = 0).toList()
        assert(result.size == 2)
        assertEquals(result.first(), Result.Loading)
        assertEquals(result[1], Result.Success(emptyList()))
    }

    @Test
    fun getByNameWithOrderDescTest() = runTest(dispatcher) {
        val repoNames = subject.getRepositoriesByName(name = "andro", page = 0, limit = 2, isDescSort = false)
            .firstSuccess()?.map { it.name }
        val expectedNames = listOf("shadowsocks-android", "awesome-android-ui")
        assertEquals(repoNames, expectedNames)
    }

    @Test
    fun getByNameWithOrderAscTest() = runTest(dispatcher) {
        val repoNames = subject.getRepositoriesByName(name = "andro", page = 0, limit = 2, isDescSort = true)
            .firstSuccess()?.map { it.name }
        val expectedNames = listOf("AndrOBD", "AndroidAutoSize")
        assertEquals(repoNames, expectedNames)
    }

    @Test
    fun getByNameWithOffsetTest() = runTest(dispatcher) {
        val repoNames1 = subject.getRepositoriesByName(name = "andro", page = 0, limit = 2)
            .firstSuccess().map { it.name }

        val repoNames2 = subject.getRepositoriesByName(name = "andro", page = 1, limit = 2)
            .firstSuccess().map { it.name }

        val repoNames3 = subject.getRepositoriesByName(name = "andro", page = 0, limit = 4)
            .firstSuccess().map { it.name }

        assertEquals(repoNames1 + repoNames2, repoNames3)
    }

    @Test
    fun getByNameOutOfOffsetTest() = runTest(dispatcher) {
        val repoNames = subject.getRepositoriesByName(name = "andro", page = 1000, limit = 1000).firstSuccess()
        assertEquals(repoNames, emptyList())
    }
    
}