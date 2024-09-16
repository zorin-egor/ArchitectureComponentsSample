package com.sample.architecturecomponents.core.data.repositories.users

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun getUsers(sinceId: Long, limit: Long = 30): Flow<Result<List<User>>>

    suspend fun insert(item: User)

    suspend fun delete(item: User)

}