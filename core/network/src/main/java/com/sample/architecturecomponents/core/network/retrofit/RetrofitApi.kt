package com.sample.architecturecomponents.core.network.retrofit

import com.sample.architecturecomponents.core.network.models.NetworkDetails
import com.sample.architecturecomponents.core.network.models.NetworkUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitApi {

    @GET("users")
    suspend fun getUsers(@Query("since") since: Long): Response<List<NetworkUser>>

    @GET
    suspend fun getDetails(@Url url: String): Response<NetworkDetails>

}