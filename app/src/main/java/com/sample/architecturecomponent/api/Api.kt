package com.sample.architecturecomponent.api

import com.sample.architecturecomponent.vo.UserItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Api {

    companion object {
        val BASE_URL: String = "https://api.github.com/"
    }

    @GET("users")
    suspend fun getUsers(@QueryMap args: Map<String, String>): Response<List<UserItem>>

}