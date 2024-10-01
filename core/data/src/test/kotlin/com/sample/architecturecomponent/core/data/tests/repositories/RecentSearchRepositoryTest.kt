package com.sample.architecturecomponent.core.data.tests.repositories

import com.sample.architecturecomponent.core.data.tests.dao.RecentSearchDaoTest
import com.sample.architecturecomponent.core.data.tests.ext.firstSuccess
import com.sample.architecturecomponents.core.data.repositories.recent_search.RecentSearchRepository
import com.sample.architecturecomponents.core.data.repositories.recent_search.RecentSearchRepositoryImpl
import com.sample.architecturecomponents.core.database.dao.RecentSearchDao
import com.sample.architecturecomponents.core.model.RecentSearch
import com.sample.architecturecomponents.core.model.RecentSearchTags
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class RecentSearchRepositoryTest {

    private val testScope = TestScope(TestCoroutineScheduler())
    private lateinit var subject: RecentSearchRepository
    private lateinit var recentSearchDao: RecentSearchDao

    @Before
    fun setup() {
        recentSearchDao = RecentSearchDaoTest()
        subject = RecentSearchRepositoryImpl(
            recentSearchDao = recentSearchDao,
            ioScope = testScope
        )
    }

    @Test
    fun repositoryEmptyTest() = runTest {
        subject.getRecentSearch(query = "value", tag = RecentSearchTags.Users).firstSuccess()
    }

    @Test
    fun repositoryInsertGetByQueryNameTest() = runTest {
        val recentSearchUsers = mutableListOf<RecentSearch>()
        repeat(5) {
            val item = RecentSearch(
                value = "value$it",
                date = Instant.fromEpochMilliseconds(it.toLong()),
                tag = RecentSearchTags.Users
            )
            recentSearchUsers.add(item)
            subject.insert(item)
        }

        val recentSearchRepoUsersResult = subject.getRecentSearch(query = "value", tag = RecentSearchTags.Users)
            .firstSuccess()

        assertEquals(recentSearchUsers, recentSearchRepoUsersResult)
    }

    @Test
    fun repositoryInsertDeleteQueryTest() = runTest {
        val item = RecentSearch(
            value = "value",
            date = Instant.fromEpochMilliseconds(1),
            tag = RecentSearchTags.Users
        )

        subject.insert(item)

        val recentSearchRepoUsers1 = subject.getRecentSearch(query = "value", tag = RecentSearchTags.Users)
            .firstSuccess().firstOrNull()

        assertEquals(item, recentSearchRepoUsers1)

        subject.delete(item)

        val recentSearchRepoUsers2 = subject.getRecentSearch(query = "value", tag = RecentSearchTags.Users)
            .firstSuccess()

        assertEquals(recentSearchRepoUsers2, emptyList())
    }

    @Test
    fun repositoryInsertDeleteAllTest() = runTest {
        val recentSearchUsers = mutableListOf<RecentSearch>()
        repeat(5) {
            val item = RecentSearch(
                value = "value$it",
                date = Instant.fromEpochMilliseconds(it.toLong()),
                tag = RecentSearchTags.Users
            )
            recentSearchUsers.add(item)
            subject.insert(item)
        }

        val recentSearchRepoUsersResult1 = subject.getRecentSearch(query = "value", tag = RecentSearchTags.Users)
            .firstSuccess()

        assertEquals(recentSearchUsers, recentSearchRepoUsersResult1)

        subject.delete()

        val recentSearchRepoUsersResult2 = subject.getRecentSearch(query = "value", tag = RecentSearchTags.Users)
            .firstSuccess()

        assertEquals(recentSearchRepoUsersResult2, emptyList())
    }

}