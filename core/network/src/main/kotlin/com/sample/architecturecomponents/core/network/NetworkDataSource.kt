package com.sample.architecturecomponents.core.network

import com.sample.architecturecomponents.core.network.models.NetworkRepositories
import com.sample.architecturecomponents.core.network.models.NetworkUser
import com.sample.architecturecomponents.core.network.models.NetworkUserDetails
import com.sample.architecturecomponents.core.network.models.common.NetworkRepository
import retrofit2.Response

interface NetworkDataSource {

    suspend fun getUsers(since: Long, perPage: Long): Response<List<NetworkUser>>

    suspend fun getUserDetails(url: String): Response<NetworkUserDetails>

    suspend fun getRepositories(name: String, page: Int, perPage: Int, sort: String?,
        isDescOrder: Boolean = true): Response<NetworkRepositories>

    suspend fun getRepositoryDetails(owner: String, repo: String): Response<NetworkRepository>

}