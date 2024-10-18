package com.sample.architecturecomponents.core.testing.tests.repositories

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.user_details.UserDetailsRepository
import com.sample.architecturecomponents.core.model.UserDetails
import com.sample.architecturecomponents.core.network.exceptions.EmptyException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


class UserDetailsRepositoryTestImpl : UserDetailsRepository {

    private val details = MutableStateFlow<UserDetails?>(null)

    override fun getDetails(userId: Long, url: String): Flow<Result<UserDetails>> {
        return details.map {
            if (it != null) {
                Result.Success(it)
            } else {
                Result.Error(EmptyException)
            }
        }.onStart {
            emit(Result.Loading)
        }
    }

    override suspend fun insert(item: UserDetails) {
        details.tryEmit(item)
    }
}