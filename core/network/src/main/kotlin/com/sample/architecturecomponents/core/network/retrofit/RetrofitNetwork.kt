package com.sample.architecturecomponents.core.network.retrofit


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sample.architecturecomponents.core.network.BuildConfig
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.models.NetworkDetails
import com.sample.architecturecomponents.core.network.models.NetworkRepositories
import com.sample.architecturecomponents.core.network.models.NetworkUser
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

internal const val BASE_URL = BuildConfig.BACKEND_URL

@Singleton
internal class RetrofitNetwork @Inject constructor(
    okHttpClient: OkHttpClient,
    json: Json
) : NetworkDataSource {

    private var api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()),)
        .build()
        .create(RetrofitApi::class.java)

    override suspend fun getUsers(since: Long, perPage: Int): Response<List<NetworkUser>> =
        api.getUsers(since = since, perPage = perPage)

    override suspend fun getDetails(url: String): Response<NetworkDetails> =
        api.getDetails(url)

    override suspend fun getRepositories(name: String, page: Int, perPage: Int): Response<NetworkRepositories> =
        api.getRepositories(query = name, page = page, perPage = perPage)
}

