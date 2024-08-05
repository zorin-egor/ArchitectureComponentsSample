package com.sample.architecturecomponents.core.data.repositories.repository_details

import com.sample.architecturecomponents.core.model.RepositoryDetails
import kotlinx.coroutines.flow.Flow

interface RepositoryDetailsRepository {

    fun getDetails(owner: String, repo: String): Flow<Result<RepositoryDetails>>

    suspend fun insert(item: RepositoryDetails)

    suspend fun delete(item: RepositoryDetails)

}