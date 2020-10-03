package com.sample.architecturecomponent.api

import com.sample.architecturecomponent.model.DetailsItem
import com.sample.architecturecomponent.model.UserItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {

    companion object {
        val BASE_URL: String = "https://api.github.com/"
    }

    @GET("users")
    suspend fun getUsers(@Query("since") since: String): Response<List<UserItem>>

    @GET
    suspend fun getDetails(@Url url: String): Response<DetailsItem>

}