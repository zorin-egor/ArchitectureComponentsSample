package com.sample.architecturecomponents.core.network

import com.sample.architecturecomponents.core.network.models.NetworkDetails
import com.sample.architecturecomponents.core.network.models.NetworkRepositories
import com.sample.architecturecomponents.core.network.models.NetworkUser
import retrofit2.Response

interface NetworkDataSource {

    suspend fun getUsers(since: Long, perPage: Long): Response<List<NetworkUser>>

    suspend fun getDetails(url: String): Response<NetworkDetails>

    suspend fun getRepositories(name: String, page: Int, perPage: Int, sort: String?,
        isDescOrder: Boolean = true): Response<NetworkRepositories>

}