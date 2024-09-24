package com.sample.architecturecomponents.core.database.dao.tests

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sample.architecturecomponents.core.database.AppDatabase
import com.sample.architecturecomponents.core.database.dao.RecentSearchDao
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.dao.RepositoryDetailsDao
import com.sample.architecturecomponents.core.database.dao.UserDetailsDao
import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.RecentSearchEntity
import com.sample.architecturecomponents.core.database.model.RepositoryDetailsEntity
import com.sample.architecturecomponents.core.database.model.RepositoryEntity
import com.sample.architecturecomponents.core.database.model.UserDetailsEntity
import com.sample.architecturecomponents.core.database.model.UserEntity
import com.sample.architecturecomponents.core.database.model.asExternalModel
import com.sample.architecturecomponents.core.model.RecentSearchTags
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AppDatabaseDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var recentSearchDao: RecentSearchDao
    private lateinit var repositoriesDao: RepositoriesDao
    private lateinit var repositoryDetailsDao: RepositoryDetailsDao
    private lateinit var userDetailsDao: UserDetailsDao
    private lateinit var usersDao: UsersDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java,
        ).build()

        recentSearchDao = db.recentSearchDao()
        repositoriesDao = db.repositoriesDao()
        repositoryDetailsDao = db.repositoryDetailsDao()
        userDetailsDao = db.userDetailsDao()
        usersDao = db.usersDao()
    }

    @After
    fun closeDb() = db.close()

    @Test
    fun recentSearchDaoFetchesItemsByQueryPublishDateTest() = runTest {
        val recentSearchEntities = mutableListOf<RecentSearchEntity>()
        repeat(5) {
            recentSearchEntities.add(
                testRecentSearchEntity(
                value = "value${it}",
                millisSinceEpoch = it.toLong(),
            )
            )
        }

        recentSearchDao.insert(recentSearchEntities)

        val savedRecentSearchEntities = recentSearchDao.getRecentSearch("value").first()

        assertEquals(
            listOf(4L, 3L, 2L, 1L, 0L),
            savedRecentSearchEntities.map {
                it.asExternalModel().date.toEpochMilliseconds()
            },
        )
    }

    @Test
    fun repositoriesDaoFetchesItemsByNameTest() = runTest {
        val repositoriesEntities = mutableListOf<RepositoryEntity>()
        repeat(5) {
            repositoriesEntities.add(testRepositoryEntity(
                repoId = it.toLong(),
                userId = it.toLong(),
                name = "repo$it",
                millisSinceEpochCreated = it.toLong(),
                millisSinceEpochUpdated = it.toLong(),
            ))
        }

        repositoriesDao.insert(repositoriesEntities)

        val savedRepositoriesEntities = repositoriesDao.getRepositoriesByName(name = "repo", 0).first()

        assertEquals(
            listOf("repo0", "repo1", "repo2", "repo3", "repo4"),
            savedRepositoriesEntities.map {
                it.asExternalModel().name
            },
        )
    }

    @Test
    fun repositoryDetailsDaoFetchesItemByOwnerAndNameTest() = runTest {
        val usersEntities = mutableListOf<RepositoryDetailsEntity>()
        repeat(5) {
            usersEntities.add(
                testRepositoryDetailsEntity(
                repoId = it.toLong(),
                userId = it.toLong(),
                name = "repo$it",
                owner = "owner$it",
                millisSinceEpochCreated = it.toLong(),
                millisSinceEpochUpdated = it.toLong(),
                millisSinceEpochPushed = it.toLong(),
            )
            )
        }

        repositoryDetailsDao.insert(usersEntities)

        val savedRepositoryDetailsEntities = repositoryDetailsDao.getDetailsByOwnerAndName(owner = "owner0", name = "repo0").first()

        assertEquals(
            "repo0",
            savedRepositoryDetailsEntities?.asExternalModel()?.name
        )
    }

    @Test
    fun usersDaoAndUserDetailsFetchesItemsByIdTest() = runTest {
        val userEntities = mutableListOf<UserEntity>()
        val userDetailsEntities = mutableListOf<UserDetailsEntity>()

        repeat(5) {
            userEntities.add(
                testUserEntity(
                userId = it.toLong(),
                login = "login$it",
            )
            )

            userDetailsEntities.add(
                testUserDetailsEntity(
                userId = it.toLong(),
                name = "name$it",
            )
            )
        }

        usersDao.insert(userEntities)
        userDetailsDao.insert(userDetailsEntities)

        val savedUserEntities = usersDao.getUserById(id = 0).first()

        assertEquals(
            0,
            savedUserEntities?.asExternalModel()?.id
        )

        val savedUserDetailsEntities = userDetailsDao.getDetailsById(id = 0).first()

        assertEquals(
            0,
            savedUserDetailsEntities?.asExternalModel()?.id
        )
    }

}

private fun testRecentSearchEntity(
    value: String,
    millisSinceEpoch: Long = 0,
) = RecentSearchEntity(
    value = value,
    date = Instant.fromEpochMilliseconds(millisSinceEpoch),
    tag = RecentSearchTags.None
)

private fun testRepositoryEntity(
    repoId: Long,
    userId: Long,
    name: String,
    millisSinceEpochCreated: Long = 0,
    millisSinceEpochUpdated: Long = 0,
) = RepositoryEntity(
    repoId = repoId,
    userId = userId,
    owner = "owner",
    avatarUrl = "avatarUrl",
    name = name,
    forks = 0,
    watchersCount = 0,
    createdAt = Instant.fromEpochMilliseconds(millisSinceEpochCreated),
    updatedAt = Instant.fromEpochMilliseconds(millisSinceEpochUpdated),
    stargazersCount = 0,
    description = "description"
)

private fun testRepositoryDetailsEntity(
    repoId: Long,
    userId: Long,
    name: String,
    owner: String,
    millisSinceEpochCreated: Long = 0,
    millisSinceEpochUpdated: Long = 0,
    millisSinceEpochPushed: Long = 0,
) = RepositoryDetailsEntity(
    repoId = repoId,
    userId = userId,
    owner = owner,
    avatarUrl = "avatarUrl",
    name = name,
    forks = 0,
    watchersCount = 0,
    createdAt = Instant.fromEpochMilliseconds(millisSinceEpochCreated),
    updatedAt = Instant.fromEpochMilliseconds(millisSinceEpochUpdated),
    pushedAt = Instant.fromEpochMilliseconds(millisSinceEpochPushed),
    stargazersCount = 0,
    description = "description",
    tagsUrl = "tagsUrl",
    branchesUrl = "branchesUrl",
    commitsUrl = "commitsUrl",
    topics = emptyList(),
    licence = null,
    defaultBranch = "defaultBranch",
    htmlUrl = "htmlUrl",
    nodeId = "nodeId"
)

private fun testUserEntity(
    login: String,
    userId: Long ,
) = UserEntity(
    userId = userId,
    nodeId = "nodeId",
    login = login,
    url = "url",
    avatarUrl = "avatarUrl",
)

private fun testUserDetailsEntity(
    userId: Long ,
    name: String,
    createdAt: Long = 0
) = UserDetailsEntity(
    userId = userId,
    name = name,
    avatarUrl = "avatarUrl",
    company = "company",
    blog = "blog",
    location = "location",
    email = "email",
    bio = "bio",
    publicRepos = 0,
    publicGists = 0,
    followers = 0,
    following = 0,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    reposUrl = "reposUrl",
    url = "url"
)