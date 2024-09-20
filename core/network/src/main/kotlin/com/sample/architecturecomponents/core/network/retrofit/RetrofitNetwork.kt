package com.sample.architecturecomponents.core.network.retrofit


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sample.architecturecomponents.core.datastore.SettingsPreference
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.ext.SORT_ASC
import com.sample.architecturecomponents.core.network.ext.SORT_DESC
import com.sample.architecturecomponents.core.network.models.NetworkRepositories
import com.sample.architecturecomponents.core.network.models.NetworkUser
import com.sample.architecturecomponents.core.network.models.NetworkUserDetails
import com.sample.architecturecomponents.core.network.models.common.NetworkRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class RetrofitNetwork @Inject constructor(
    okHttpClient: OkHttpClient,
    json: Json,
    settingsPreference: SettingsPreference
) : NetworkDataSource {

    private var api = Retrofit.Builder()
        .baseUrl(runBlocking { settingsPreference.getBaseUrl() })
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()),)
        .build()
        .create(RetrofitApi::class.java)

    override suspend fun getUsers(since: Long, perPage: Long): Response<List<NetworkUser>> =
        api.getUsers(since = since, perPage = perPage)

    override suspend fun getUserDetails(url: String): Response<NetworkUserDetails> =
        api.getDetails(url)

    override suspend fun getRepositories(name: String, page: Int, perPage: Int, sort: String?,
                                         isDescOrder: Boolean): Response<NetworkRepositories> =
        api.getRepositories(query = name, page = page, perPage = perPage, sort = sort,
            order = if (isDescOrder) SORT_DESC else SORT_ASC)

    override suspend fun getRepositoryDetails(owner: String, repo: String): Response<NetworkRepository> =
        api.getRepositoryDetails(owner = owner, repo = repo)
}

