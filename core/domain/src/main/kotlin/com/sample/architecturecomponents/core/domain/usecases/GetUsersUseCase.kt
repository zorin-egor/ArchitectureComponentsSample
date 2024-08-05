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

    operator fun invoke(id: Long): Flow<Result<List<User>>> {
        Timber.d("invoke($hasNext, $id) - new")

        return usersRepository.getUsers(sinceId = id, limit = LIMIT)
            .map { new ->
                val items = new.getOrNull()
                if (new.isSuccess && items?.isNotEmpty() == true) {
                    Timber.d("invoke($id) - map: ${items.size}")
                    synchronized(mutex) {
                        hasNext = items.size >= LIMIT
                        lastId = items.lastOrNull()?.id ?: lastId
                        users.clear()
                        users.addAll(items)
                        Result.success(users.toList())
                    }
                } else {
                    new
                }
            }
            .flowOn(dispatcher)
    }

    operator fun invoke(): Flow<Result<List<User>>> {
        Timber.d("invoke($hasNext, $lastId) - next")

        synchronized(mutex) {
            if (!hasNext) {
                return flowOf(Result.success(users.toList()))
            }
        }

        return usersRepository.getUsers(sinceId = lastId, limit = LIMIT)
            .map { new ->
                val items = new.getOrNull()
                if (new.isSuccess && items?.isNotEmpty() == true) {
                    Timber.d("invoke() - map: ${items.size}")
                    synchronized(mutex) {
                        hasNext = items.size >= LIMIT
                        lastId = items.lastOrNull()?.id ?: lastId
                        items.forEach { item ->
                            if (users.find { it.id == item.id } == null) {
                                users.add(item)
                            }
                        }
                        Result.success(users.toList())
                    }
                } else {
                    new
                }
            }
            .flowOn(dispatcher)
    }

}
