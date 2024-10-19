package com.sample.architecturecomponents.core.domain.usecases

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.users.UsersRepository
import com.sample.architecturecomponents.core.di.Dispatcher
import com.sample.architecturecomponents.core.di.Dispatchers
import com.sample.architecturecomponents.core.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.annotations.TestOnly
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject


class GetUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    @Dispatcher(Dispatchers.IO) private val dispatcher: CoroutineDispatcher,
) {

    companion object {
        const val SINCE_ID = 0L
        private const val LIMIT = 30L
        private const val UNDEFINED = -1L
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

    private var sinceId = AtomicLong(UNDEFINED)
    private var lastId = AtomicLong(SINCE_ID)
    private var limit = LIMIT

    operator fun invoke(id: Long = lastId.get()): Flow<Result<List<User>>> {
        Timber.d("GetUsersUseCase($id)")
        sinceId.compareAndSet(UNDEFINED, id)
        return usersRepository.getUsers(sinceId = sinceId.get(), lastId = id, limit = limit)
            .onEach { new ->
                if (new is Result.Success) {
                    new.data.lastOrNull()?.id?.let(lastId::set)
                }
            }.flowOn(dispatcher)
    }

}
