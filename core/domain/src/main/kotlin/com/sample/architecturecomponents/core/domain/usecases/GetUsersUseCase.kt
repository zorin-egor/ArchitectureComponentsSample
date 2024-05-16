package com.sample.architecturecomponents.core.domain.usecases

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
        private const val LIMIT = 30L
    }

    private val users = ArrayList<User>()
    private val mutex = Any()
    private var lastId = SINCE_ID
    private var hasNext = true

    operator fun invoke(id: Long): Flow<List<User>> {
        Timber.d("invoke($hasNext, $id) - new")

        return usersRepository.getUsers(sinceId = id, limit = LIMIT)
            .map { new ->
                synchronized(mutex) {
                    hasNext = new.size >= LIMIT
                    lastId = new.lastOrNull()?.id ?: lastId
                    users.clear()
                    users.addAll(new)
                    users.toList()
                }
            }
            .flowOn(dispatcher)
    }

    operator fun invoke(): Flow<List<User>> {
        Timber.d("invoke($hasNext, $lastId) - next")

        synchronized(mutex) {
            if (!hasNext) {
                return flowOf(users.toList())
            }
        }

        return usersRepository.getUsers(sinceId = lastId, limit = LIMIT)
            .map { new ->
                synchronized(mutex) {
                    hasNext = new.size >= LIMIT
                    lastId = new.lastOrNull()?.id ?: lastId
                    new.forEach { item ->
                        if (users.find { it.id == item.id } == null) {
                            users.add(item)
                        }
                    }
                    users.toList()
                }
            }
            .flowOn(dispatcher)
    }

}
