package com.sample.architecturecomponent.repositories.users

import com.sample.architecturecomponent.models.Container
import com.sample.architecturecomponent.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun getUsers(isForceUpdate: Boolean = false): Flow<Container<List<User>>>

    suspend fun getNextUsers(): Container<List<User>>

    suspend fun addUser(item: User): Container<Unit>

    suspend fun removeUser(item: User): Container<Unit>

}