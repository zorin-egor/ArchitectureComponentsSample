package com.sample.architecturecomponents.core.network

import com.sample.architecturecomponents.core.network.models.NetworkDetails
import com.sample.architecturecomponents.core.network.models.NetworkUser
import retrofit2.Response

interface NetworkDataSource {

    suspend fun getUsers(since: Long): Response<List<NetworkUser>>

    suspend fun getDetails(url: String): Response<NetworkDetails>

}