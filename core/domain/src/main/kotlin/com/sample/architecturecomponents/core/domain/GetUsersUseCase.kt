package com.sample.architecturecomponents.core.domain

import com.sample.architecturecomponents.core.data.repositories.users.UsersRepository
import com.sample.architecturecomponents.core.model.User
import com.sample.architecturecomponents.core.network.Dispatcher
import com.sample.architecturecomponents.core.network.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.TreeSet
import javax.inject.Inject


@OptIn(FlowPreview::class)
class GetUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {
    companion object {
        private const val SINCE_ID = 0L
    }

    private var sinceLastId: Long = SINCE_ID
    private val usersList = TreeSet<User> { o1, o2 -> o1.id.compareTo(o2.id) }

    operator fun invoke(sinceId: Long? = sinceLastId): Flow<List<User>> =
        usersRepository.getUsers(sinceId ?: SINCE_ID)
            .map {
                if (sinceId == 0L) {
                    usersList.clear()
                }
                sinceLastId = it.last().id
                usersList.addAll(it)
                usersList.toList()
            }
            .flowOn(dispatcher)
}
