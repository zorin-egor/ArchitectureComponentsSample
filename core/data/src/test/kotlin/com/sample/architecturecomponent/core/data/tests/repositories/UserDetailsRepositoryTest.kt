package com.sample.architecturecomponent.core.data.tests.repositories

import JvmUnitTestDevAssetManager
import com.sample.architecturecomponent.core.data.tests.dao.UserDetailsDaoTest
import com.sample.architecturecomponent.core.data.tests.dao.UsersDaoTest
import com.sample.architecturecomponent.core.data.tests.ext.firstSuccess
import com.sample.architecturecomponents.core.data.repositories.user_details.UserDetailsRepository
import com.sample.architecturecomponents.core.data.repositories.user_details.UserDetailsRepositoryImpl
import com.sample.architecturecomponents.core.database.dao.UserDetailsDao
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.model.UserDetails
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.dev.DevNetworkDataSource
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UserDetailsRepositoryTest {

    private val scope = TestScope(TestCoroutineScheduler())
    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    private lateinit var subject: UserDetailsRepository
    private lateinit var network: NetworkDataSource
    private lateinit var userDetailsDaoTest: UserDetailsDao
    private lateinit var userDaoTest: UsersDao

    @Before
    fun setup() {
        userDetailsDaoTest = UserDetailsDaoTest()
        userDaoTest = UsersDaoTest()

        network = DevNetworkDataSource(
            dispatcher = dispatcher,
            networkJson = Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            },
            assets = JvmUnitTestDevAssetManager,
        )

        subject = UserDetailsRepositoryImpl(
            usersDao = userDaoTest,
            detailsDao = userDetailsDaoTest,
            ioScope = scope,
            networkDatasource = network,
        )
    }

    @Test
    fun getUsersSinceTest() = runTest(dispatcher) {
        val user = subject.getDetails(userId = 0, url = "url").firstSuccess()
        assertEquals(user, UserDetails(
            id = 1,
            url = "https://github.com/mojombo",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            name = "Tom Preston-Werner",
            company = "@chatterbugapp, @redwoodjs, @preston-werner-ventures ",
            blog = "http://tom.preston-werner.com",
            location = "San Francisco",
            email = null,
            bio = null,
            publicRepos = 66,
            publicGists = 62,
            followers = 24019,
            following = 11,
            createdAt = Instant.parse("2007-10-20T05:24:19Z"),
            updatedAt = Instant.parse("2024-09-04T23:40:30Z"),
            reposUrl = "https://api.github.com/users/mojombo/repos",
            hireable=false
        ))
    }
    
}