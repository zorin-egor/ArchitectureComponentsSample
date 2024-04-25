package com.sample.architecturecomponents.core.domain

import com.sample.architecturecomponents.core.data.repositories.users.UsersRepository
import com.sample.architecturecomponents.core.model.User
import com.sample.architecturecomponents.core.network.Dispatcher
import com.sample.architecturecomponents.core.network.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject


class GetUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {
    companion object {
        const val SINCE_ID = 0L
        private const val LIMIT = 30
    }

    private val usersSet = LinkedHashSet<User>()
    private val mutex = Any()
    private var lastId = SINCE_ID
    private var hasNext = true

    operator fun invoke(id: Long): Flow<List<User>> {
        Timber.d("invoke($hasNext, $id) - new")

        return usersRepository.getUsers(sinceId = id, limit = LIMIT)
            .map {
                synchronized(mutex) {
                    hasNext = it.size >= LIMIT
                    lastId = it.lastOrNull()?.id ?: lastId
                    usersSet.clear()
                    usersSet.addAll(it)
                    usersSet.toList()
                }
            }
            .flowOn(dispatcher)
    }

    operator fun invoke(): Flow<List<User>> {
        Timber.d("invoke($hasNext, $lastId) - next")

        synchronized(mutex) {
            if (!hasNext) {
                return flowOf(usersSet.toList())
            }
        }

        return usersRepository.getUsers(sinceId = lastId, limit = LIMIT)
            .map {
                synchronized(mutex) {
                    hasNext = it.size >= LIMIT
                    lastId = it.lastOrNull()?.id ?: lastId
                    usersSet.addAll(it)
                    usersSet.toList()
                }
            }
            .flowOn(dispatcher)
    }

}
