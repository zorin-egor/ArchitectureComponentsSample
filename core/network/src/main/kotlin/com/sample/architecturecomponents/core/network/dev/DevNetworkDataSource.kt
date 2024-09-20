package com.sample.architecturecomponents.core.network.dev

import JvmUnitTestDevAssetManager
import com.sample.architecturecomponents.core.network.Dispatcher
import com.sample.architecturecomponents.core.network.Dispatchers
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
import retrofit2.Response
import java.io.InputStream
import javax.inject.Inject

class DevNetworkDataSource @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: DevAssetManager = JvmUnitTestDevAssetManager,
) : NetworkDataSource {

    companion object {
        private const val GITHUB_REPOSITORIES = "github_repositories.json"
        private const val GITHUB_USER_DETAILS = "github_user_details.json"
        private const val GITHUB_USERS = "github_users.json"
        private const val GITHUB_USER_REPOSITORIES = "github_user_repository.json"
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getUsers(since: Long, perPage: Long): Response<List<NetworkUser>> {
        return withContext(ioDispatcher) {
            assets.open(GITHUB_USERS).use<InputStream, List<NetworkUser>>(networkJson::decodeFromStream)
                .let { Response.success(it) }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getUserDetails(url: String): Response<NetworkUserDetails> {
        return withContext(ioDispatcher) {
            assets.open(GITHUB_USER_DETAILS).use<InputStream, NetworkUserDetails>(networkJson::decodeFromStream)
                .let { Response.success(it) }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getRepositories(name: String, page: Int, perPage: Int, sort: String?, isDescOrder: Boolean): Response<NetworkRepositories> {
        return withContext(ioDispatcher) {
            assets.open(GITHUB_REPOSITORIES).use<InputStream, NetworkRepositories>(networkJson::decodeFromStream)
                .let { Response.success(it) }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getRepositoryDetails(owner: String, repo: String): Response<NetworkRepository> {
        return withContext(ioDispatcher) {
            assets.open(GITHUB_USER_REPOSITORIES).use<InputStream, NetworkRepository>(networkJson::decodeFromStream)
                .let { Response.success(it) }
        }
    }
}
