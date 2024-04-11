package com.sample.architecturecomponents.core.data.repositories.users

import com.sample.architecturecomponents.core.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun getUsers(sinceId: Long): Flow<List<User>>

    suspend fun add(item: User): Unit

    suspend fun remove(item: User): Unit

}