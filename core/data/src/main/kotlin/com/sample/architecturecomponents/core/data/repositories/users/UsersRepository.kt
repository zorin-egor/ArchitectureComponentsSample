package com.sample.architecturecomponents.core.data.repositories.users

import com.sample.architecturecomponents.core.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun getUsers(sinceId: Long, limit: Int = 30): Flow<List<User>>

    suspend fun add(item: User)

    suspend fun remove(item: User)

}