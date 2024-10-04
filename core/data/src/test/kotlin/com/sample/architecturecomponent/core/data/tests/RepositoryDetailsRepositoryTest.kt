package com.sample.architecturecomponent.core.data.tests

import JvmUnitTestDevAssetManager
import com.sample.architecturecomponent.core.data.tests.dao.RepositoriesDaoTestImpl
import com.sample.architecturecomponent.core.data.tests.dao.RepositoryDetailsDaoTestImpl
import com.sample.architecturecomponent.core.data.tests.ext.firstSuccess
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.repository_details.RepositoryDetailsRepository
import com.sample.architecturecomponents.core.data.repositories.repository_details.RepositoryDetailsRepositoryImpl
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.dao.RepositoryDetailsDao
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.dev.DevNetworkDataSource
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class RepositoryDetailsRepositoryTest {

    private val scope = TestScope(TestCoroutineScheduler())
    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private lateinit var subject: RepositoryDetailsRepository
    private lateinit var network: NetworkDataSource
    private lateinit var repositoryDetailsDaoTest: RepositoryDetailsDao
    private lateinit var repositoriesDaoTest: RepositoriesDao

    @Before
    fun setup() {
        repositoryDetailsDaoTest = RepositoryDetailsDaoTestImpl()
        repositoriesDaoTest = RepositoriesDaoTestImpl()

        network = DevNetworkDataSource(
            dispatcher = dispatcher,
            networkJson = Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            },
            assets = JvmUnitTestDevAssetManager,
        )

        subject = RepositoryDetailsRepositoryImpl(
            repositoryDetailsDao = repositoryDetailsDaoTest,
            ioScope = scope,
            networkDatasource = network,
        )
    }

    @Test
    fun getUsersSinceTest() = runTest(dispatcher) {
        val result = subject.getDetails(owner = "owner", repo = "repo").firstSuccess()
        assertEquals(RepositoryDetails(
            id = 26899533, 
            userId = 1, 
            userLogin = "mojombo",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            name = "30daysoflaptops.github.io",
            htmlUrl = "https://github.com/mojombo/30daysoflaptops.github.io",
            nodeId = "MDEwOlJlcG9zaXRvcnkyNjg5OTUzMw==",
            forks = 4, 
            watchersCount = 8, 
            createdAt = Instant.parse("2014-11-20T06:42:06Z"),
            updatedAt = Instant.parse("2023-11-29T23:19:06Z"),
            pushedAt = Instant.parse("2014-11-20T06:42:47Z"),
            defaultBranch = "gh-pages",
            stargazersCount = 8, 
            description = null, 
            tagsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/tags",
            branchesUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/branches{/branch}",
            commitsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/commits{/sha}",
            topics = emptyList(),
            license = null
        ), result)
    }

    @Test
    fun getRepositoryDetailsErrorTest() = runTest(dispatcher) {
        val user = subject.getDetails(owner = "", repo = "").toList()
        assertEquals(Result.Loading, user[0] as Result.Loading)
        assert(user[1] is Result.Error)
    }

    @Test
    fun getUserDetailsRepoTest() = runTest(dispatcher) {
        val item = RepositoryDetails(
            id = 26899533,
            userId = 1,
            userLogin = "mojombo",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            name = "30daysoflaptops.github.io",
            htmlUrl = "https://github.com/mojombo/30daysoflaptops.github.io",
            nodeId = "MDEwOlJlcG9zaXRvcnkyNjg5OTUzMw==",
            forks = 4,
            watchersCount = 8,
            createdAt = Instant.parse("2014-11-20T06:42:06Z"),
            updatedAt = Instant.parse("2023-11-29T23:19:06Z"),
            pushedAt = Instant.parse("2014-11-20T06:42:47Z"),
            defaultBranch = "gh-pages",
            stargazersCount = 8,
            description = null,
            tagsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/tags",
            branchesUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/branches{/branch}",
            commitsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/commits{/sha}",
            topics = emptyList(),
            license = null
        )

        subject.insert(item)

        val users = subject.getDetails(owner = "mojombo", repo = "30daysoflaptops.github.io").toList()

        assertEquals(2, users.size)
        assertEquals(Result.Loading, users[0] as Result.Loading)
        assert(users.single { it is Result.Success } is Result.Success)
    }
    
}