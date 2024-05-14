package com.sample.architecturecomponents.core.network.retrofit

import com.sample.architecturecomponents.core.network.models.NetworkDetails
import com.sample.architecturecomponents.core.network.models.NetworkRepositories
import com.sample.architecturecomponents.core.network.models.NetworkUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitApi {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Long,
        @Query("per_page") perPage: Long = 30
    ): Response<List<NetworkUser>>

    @GET
    suspend fun getDetails(@Url url: String): Response<NetworkDetails>

    @GET("search/repositories")
    suspend fun getRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
    ): Response<NetworkRepositories>

}