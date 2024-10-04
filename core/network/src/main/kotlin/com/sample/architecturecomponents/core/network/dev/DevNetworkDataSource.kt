package com.sample.architecturecomponents.core.network.dev

import JvmUnitTestDevAssetManager
import com.sample.architecturecomponents.core.common.extensions.safeSubList
import com.sample.architecturecomponents.core.di.Dispatcher
import com.sample.architecturecomponents.core.di.Dispatchers
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.models.NetworkRepositories
import com.sample.architecturecomponents.core.network.models.NetworkUser
import com.sample.architecturecomponents.core.network.models.NetworkUserDetails
import com.sample.architecturecomponents.core.network.models.common.NetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.InputStream
import javax.inject.Inject

class DevNetworkDataSource @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: DevAssetManager = JvmUnitTestDevAssetManager,
) : NetworkDataSource {

    companion object {
        private const val GITHUB_REPOSITORIES = "github_repositories.json"
        private const val GITHUB_USER_DETAILS = "github_user_details.json"
        private const val GITHUB_USERS = "github_users.json"
        private const val GITHUB_REPOSITORY_DETAILS = "github_repository_details.json"
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getUsers(since: Long, perPage: Long): Response<List<NetworkUser>> {
        return withContext(dispatcher) {
            assets.open(GITHUB_USERS).use<InputStream, List<NetworkUser>>(networkJson::decodeFromStream)
                .run {
                    val index = indexOfFirst { it.id == since }
                    val maxId = maxOf { it.id }
                    when {
                        index < 0 && since > maxId -> emptyList()
                        index < 0 && since < maxId ->safeSubList(0, perPage.toInt())
                        else -> safeSubList(index + 1, index + 1 + perPage.toInt())
                    }
                }.let {
                    Response.success(it)
                }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getUserDetails(url: String): Response<NetworkUserDetails> {
        if (url.isEmpty()) {
            return Response.error(404, "".toResponseBody())
        }

        return withContext(dispatcher) {
            assets.open(GITHUB_USER_DETAILS).use<InputStream, NetworkUserDetails>(networkJson::decodeFromStream)
                .let { Response.success(it) }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getRepositories(name: String, page: Int, perPage: Int, sort: String?, isDescOrder: Boolean): Response<NetworkRepositories> {
        return withContext(dispatcher) {
            assets.open(GITHUB_REPOSITORIES).use<InputStream, NetworkRepositories>(networkJson::decodeFromStream)
                .let { network ->
                    val newRepositories = network.networkRepositories
                        .filter { it.name.contains(other = name, ignoreCase = true) }
                        .run {
                            when {
                                sort != null && isDescOrder -> sortedBy(NetworkRepository::name)
                                sort != null && !isDescOrder -> sortedByDescending(NetworkRepository::name)
                                else -> this
                            }
                        }.safeSubList(page * perPage, page * perPage + perPage)

                    network.copy(networkRepositories = newRepositories)
                }
                .let { Response.success(it) }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getRepositoryDetails(owner: String, repo: String): Response<NetworkRepository> {
        if (owner.isEmpty() || repo.isEmpty()) {
            return Response.error(404, "".toResponseBody())
        }

        return withContext(dispatcher) {
            assets.open(GITHUB_REPOSITORY_DETAILS).use<InputStream, NetworkRepository>(networkJson::decodeFromStream)
                .let { Response.success(it) }
        }
    }
}
