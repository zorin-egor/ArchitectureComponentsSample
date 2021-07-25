package com.sample.architecturecomponent.repositories.users

import com.sample.architecturecomponent.models.Container
import com.sample.architecturecomponent.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun getData(): Flow<Container<List<User>>>

    suspend fun reset(): Container<List<User>>

    suspend fun next(): Container<List<User>>

    suspend fun add(item: User): Container<Unit>

    suspend fun remove(item: User): Container<Unit>

}