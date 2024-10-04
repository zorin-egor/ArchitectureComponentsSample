package com.sample.architecturecomponents.core.testing.tests.repositories

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.user_details.UserDetailsRepository
import com.sample.architecturecomponents.core.model.UserDetails
import com.sample.architecturecomponents.core.network.exceptions.NetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take


class UserDetailsRepositoryTestImpl : UserDetailsRepository {

    private val details = MutableStateFlow<UserDetails?>(null)

    override fun getDetails(userId: Long, url: String): Flow<Result<UserDetails>> {
        return details.map {
            if (it != null) {
                Result.Success(it)
            } else {
                Result.Error(NetworkException(404))
            }
        }.onStart {
            emit(Result.Loading)
        }.take(2)
    }

    override suspend fun insert(item: UserDetails) {
        details.tryEmit(item)
    }
}