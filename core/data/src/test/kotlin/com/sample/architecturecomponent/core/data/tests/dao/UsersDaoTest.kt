package com.sample.architecturecomponent.core.data.tests.dao

import com.sample.architecturecomponents.core.database.dao.UsersDao
import com.sample.architecturecomponents.core.database.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class UsersDaoTest : UsersDao {

    private val dbStateFlow = MutableStateFlow(emptyList<UserEntity>())

    override fun getUsers(): Flow<List<UserEntity>> = dbStateFlow

    override fun getUserById(id: Long): Flow<UserEntity?> =
        dbStateFlow.map { flow ->
            flow.filter { it.userId == id }.let {
                if (it.size > 1) throw IllegalStateException("More then 1 matches")
                it.firstOrNull()
            }
        }

    override fun getUsersCount(from: Long, count: Long): Flow<List<UserEntity>> =
        dbStateFlow.map { flow ->
            flow.runCatching { subList(from.toInt(), (from + count).toInt()) }.getOrNull()
                ?: emptyList()
        }

    override fun getUsersSinceId(sinceId: Long, limit: Long): Flow<List<UserEntity>> =
        dbStateFlow.map { flow ->
            val userIndex = flow.indexOfFirst { it.userId == sinceId }
            runCatching { flow.subList(userIndex, userIndex + limit.toInt()) }.getOrNull()
                ?: emptyList()
        }

    override suspend fun insert(item: UserEntity) =
        dbStateFlow.update { flow -> (flow + item).distinctBy(UserEntity::userId) }

    override suspend fun insert(items: List<UserEntity>) =
        dbStateFlow.update { flow -> (flow + items).distinctBy(UserEntity::userId) }

    override suspend fun delete(item: UserEntity) =
        dbStateFlow.update { flow -> flow.toMutableList().apply { remove(item) } }

    override suspend fun delete() =
        dbStateFlow.update { flow -> flow.toMutableList().apply { clear() } }

}