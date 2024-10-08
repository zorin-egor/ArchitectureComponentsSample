package com.sample.architecturecomponents.core.network.retrofit

import com.sample.architecturecomponents.core.network.models.NetworkRepositories
import com.sample.architecturecomponents.core.network.models.NetworkUser
import com.sample.architecturecomponents.core.network.models.NetworkUserDetails
import com.sample.architecturecomponents.core.network.models.common.NetworkRepository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitApi {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Long,
        @Query("per_page") perPage: Long = 30
    ): Response<List<NetworkUser>>

    @GET
    suspend fun getDetails(@Url url: String): Response<NetworkUserDetails>

    @GET("search/repositories")
    suspend fun getRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
    ): Response<NetworkRepositories>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<NetworkRepository>

}