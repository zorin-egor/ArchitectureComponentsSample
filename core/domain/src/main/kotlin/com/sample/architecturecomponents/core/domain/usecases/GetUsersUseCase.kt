package com.sample.architecturecomponents.core.domain.usecases

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.users.UsersRepository
import com.sample.architecturecomponents.core.di.Dispatcher
import com.sample.architecturecomponents.core.di.Dispatchers
import com.sample.architecturecomponents.core.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.jetbrains.annotations.TestOnly
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject


class GetUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher,
) {

    companion object {
        const val SINCE_ID = 0L
        private const val LIMIT = 30L
    }

    @TestOnly
    constructor(
        usersRepository: UsersRepository,
        @Dispatcher(Dispatchers.IO) dispatcher: CoroutineDispatcher,
        limitPerPage: Long = LIMIT
    ) : this(
        usersRepository = usersRepository,
        dispatcher = dispatcher
    ) {
        limit = limitPerPage
    }

    private val lock = Any()
    private val users = ArrayList<User>()
    private var lastId = AtomicLong(SINCE_ID)
    private var hasNext = AtomicBoolean(true)
    private var limit = LIMIT

    operator fun invoke(id: Long = lastId.get()): Flow<Result<List<User>>> {
        Timber.d("GetUsersUseCase($id)")

        synchronized(lock) {
            if (id == lastId.get() && !hasNext.get()) {
                Timber.d("GetUsersUseCase() - no more: ${hasNext.get()}")
                return flowOf(Result.Success(users.toList()))
            }
        }

        return usersRepository.getUsers(sinceId = id, limit = limit)
            .map { new ->
                when(new) {
                    is Result.Success -> {
                        val items = synchronized(lock) {
                            hasNext.getAndSet(new.data.size >= limit)
                            if (new.data.isEmpty()) return@synchronized users.toList()
                            if (id != lastId.get()) users.clear()
                            lastId.set(new.data.last().id)
                            new.data.forEach { item ->
                                if (users.find { it.id == item.id } == null) {
                                    users.add(item)
                                }
                            }
                            users.toList()
                        }

                        Timber.d("GetUsersUseCase() - items: ${users.size}")
                        Result.Success(items)
                    }
                    else -> new
                }
            }.flowOn(dispatcher)
    }

}
