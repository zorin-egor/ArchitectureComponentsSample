package com.sample.architecturecomponents.core.testing.tests.repositories

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.repository_details.RepositoryDetailsRepository
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.core.network.exceptions.NetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


class RepositoryDetailsRepositoryTestImpl : RepositoryDetailsRepository {

    private val details = MutableStateFlow<RepositoryDetails?>(null)

    override fun getDetails(owner: String, repo: String): Flow<Result<RepositoryDetails>> =
        details.map {
            if (it != null) {
                Result.Success(it)
            } else {
                Result.Error(NetworkException(errorCode = 404))
            }
        }.onStart {
            emit(Result.Loading)
        }

    override suspend fun insert(item: RepositoryDetails) {
        details.tryEmit(item)
    }

    override suspend fun delete(item: RepositoryDetails) {
        details.tryEmit(null)
    }

}