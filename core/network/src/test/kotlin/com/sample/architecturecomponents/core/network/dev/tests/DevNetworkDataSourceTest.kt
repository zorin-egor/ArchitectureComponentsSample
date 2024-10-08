package com.sample.architecturecomponents.core.network.dev.tests

import JvmUnitTestDevAssetManager
import com.sample.architecturecomponents.core.network.dev.DevNetworkDataSource
import com.sample.architecturecomponents.core.network.models.NetworkUser
import com.sample.architecturecomponents.core.network.models.NetworkUserDetails
import com.sample.architecturecomponents.core.network.models.common.NetworkRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class DevNetworkDataSourceTest {

    private lateinit var subject: DevNetworkDataSource

    private val testDispatcher = StandardTestDispatcher()

    private val user: NetworkUser
        get() = NetworkUser(
            id = 1,
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            eventsUrl = "https://api.github.com/users/mojombo/events{/privacy}",
            followersUrl = "https://api.github.com/users/mojombo/followers",
            followingUrl = "https://api.github.com/users/mojombo/following{/other_user}",
            gistsUrl = "https://api.github.com/users/mojombo/gists{/gist_id}",
            gravatarId = "",
            htmlUrl = "https://github.com/mojombo",
            login = "mojombo",
            nodeId = "MDQ6VXNlcjE=",
            organizationsUrl = "https://api.github.com/users/mojombo/orgs",
            receivedEventsUrl = "https://api.github.com/users/mojombo/received_events",
            reposUrl = "https://api.github.com/users/mojombo/repos",
            siteAdmin = false,
            starredUrl = "https://api.github.com/users/mojombo/starred{/owner}{/repo}",
            subscriptionsUrl = "https://api.github.com/users/mojombo/subscriptions",
            type = "User",
            url = "https://api.github.com/users/mojombo"
        )
    
    private val details: NetworkUserDetails
        get() = NetworkUserDetails(
            id = 1,
            login = "mojombo",
            nodeId = "MDQ6VXNlcjE=",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            gravatarId = "",
            url = "https://api.github.com/users/mojombo",
            htmlUrl = "https://github.com/mojombo",
            followersUrl = "https://api.github.com/users/mojombo/followers",
            followingUrl = "https://api.github.com/users/mojombo/following{/other_user}",
            gistsUrl = "https://api.github.com/users/mojombo/gists{/gist_id}",
            starredUrl = "https://api.github.com/users/mojombo/starred{/owner}{/repo}",
            subscriptionsUrl = "https://api.github.com/users/mojombo/subscriptions",
            organizationsUrl = "https://api.github.com/users/mojombo/orgs",
            reposUrl = "https://api.github.com/users/mojombo/repos",
            eventsUrl = "https://api.github.com/users/mojombo/events{/privacy}",
            receivedEventsUrl = "https://api.github.com/users/mojombo/received_events",
            type = "User",
            siteAdmin = false,
            name = "Tom Preston-Werner",
            company = "@chatterbugapp, @redwoodjs, @preston-werner-ventures ",
            blog = "http://tom.preston-werner.com",
            location = "San Francisco",
            email = null,
            hireable = null,
            bio = null,
            twitterUsername = "mojombo",
            publicRepos = 66,
            publicGists = 62,
            followers = 24019,
            following = 11,
            createdAt = "2007-10-20T05:24:19Z",
            updatedAt = "2024-09-04T23:40:30Z"
        )

    private val repository: NetworkRepository
        get() = NetworkRepository(
            id = 26899533,
            nodeId = "MDEwOlJlcG9zaXRvcnkyNjg5OTUzMw==",
            name = "30daysoflaptops.github.io",
            fullName = "mojombo/30daysoflaptops.github.io",
            private = false,
            htmlUrl = "https://github.com/mojombo/30daysoflaptops.github.io",
            description = null,
            fork = false,
            url = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io",
            forksUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/forks",
            keysUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/keys{/key_id}",
            collaboratorsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/collaborators{/collaborator}",
            teamsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/teams",
            hooksUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/hooks",
            issueEventsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/issues/events{/number}",
            eventsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/events",
            assigneesUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/assignees{/user}",
            branchesUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/branches{/branch}",
            tagsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/tags",
            blobsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/git/blobs{/sha}",
            gitTagsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/git/tags{/sha}",
            gitRefsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/git/refs{/sha}",
            treesUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/git/trees{/sha}",
            statusesUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/statuses/{sha}",
            languagesUrl =  "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/languages",
            stargazersUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/stargazers",
            contributorsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/contributors",
            subscribersUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/subscribers",
            subscriptionUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/subscription",
            commitsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/commits{/sha}",
            gitCommitsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/git/commits{/sha}",
            commentsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/comments{/number}",
            issueCommentUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/issues/comments{/number}",
            contentsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/contents/{+path}",
            compareUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/compare/{base}...{head}",
            mergesUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/merges",
            archiveUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/{archive_format}{/ref}",
            downloadsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/downloads",
            issuesUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/issues{/number}",
            pullsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/pulls{/number}",
            milestonesUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/milestones{/number}",
            notificationsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/notifications{?since,all,participating}",
            labelsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/labels{/name}",
            releasesUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/releases{/id}",
            deploymentsUrl = "https://api.github.com/repos/mojombo/30daysoflaptops.github.io/deployments",
            createdAt = "2014-11-20T06:42:06Z",
            updatedAt = "2023-11-29T23:19:06Z",
            pushedAt = "2014-11-20T06:42:47Z",
            gitUrl = "git://github.com/mojombo/30daysoflaptops.github.io.git",
            sshUrl = "git@github.com:mojombo/30daysoflaptops.github.io.git",
            cloneUrl = "https://github.com/mojombo/30daysoflaptops.github.io.git",
            svnUrl = "https://github.com/mojombo/30daysoflaptops.github.io",
            homepage = null,
            size = 1197,
            stargazersCount = 8,
            watchersCount = 8,
            language = "CSS",
            hasIssues = false,
            hasProjects = true,
            hasDownloads = true,
            hasWiki = true,
            hasPages = false,
            hasDiscussions = false,
            forksCount = 4,
            mirrorUrl = null,
            archived = false,
            disabled = false,
            openIssuesCount = 0,
            networkLicense = null,
            allowForking = true,
            isTemplate = false,
            webCommitSignoffRequired = false,
            topics = emptyList(),
            visibility = "public",
            forks = 4,
            openIssues = 0,
            watchers = 8,
            defaultBranch = "gh-pages",
            score = null,
            owner = user
        )

    @Before
    fun setUp() {
        subject = DevNetworkDataSource(
            dispatcher = testDispatcher,
            networkJson = Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            },
            assets = JvmUnitTestDevAssetManager,
        )
    }

    @Test
    fun getUserTest() = runTest(testDispatcher) {
        assertEquals(user, subject.getUsers(since = 0, perPage = 10).body()?.first())
    }

    @Test
    fun getRepositoryDetailsTest() = runTest(testDispatcher) {
        assertEquals(repository, subject.getRepositoryDetails(owner = "owner", repo = "repo").body())
    }

    @Test
    fun getUserDetailsTest() = runTest(testDispatcher) {
        assertEquals(details, subject.getUserDetails(url = "url").body())
    }

    @Test
    fun getUserDetailsErrorTest() = runTest(testDispatcher) {
        assertEquals(404, subject.getUserDetails(url = "").code())
    }

    @Test
    fun getRepositoryDetailsErrorTest() = runTest(testDispatcher) {
        assertEquals(404, subject.getRepositoryDetails(owner = "", repo = "").code())
    }

}
