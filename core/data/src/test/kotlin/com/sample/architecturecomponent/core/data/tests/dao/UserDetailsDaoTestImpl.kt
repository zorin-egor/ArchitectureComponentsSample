package com.sample.architecturecomponent.core.data.tests.dao

import com.sample.architecturecomponents.core.database.dao.UserDetailsDao
import com.sample.architecturecomponents.core.database.model.UserDetailsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class UserDetailsDaoTestImpl : UserDetailsDao {

    private val dbStateFlow = MutableStateFlow(emptyList<UserDetailsEntity>())

    override fun getDetailsById(id: Long): Flow<UserDetailsEntity?> =
        dbStateFlow.map { flow ->
            flow.filter { it.userId == id }.let {
                if (it.size > 1) throw IllegalStateException("More then 1 matches")
                it.firstOrNull()
            }
        }

    override suspend fun insert(item: UserDetailsEntity) =
        dbStateFlow.update { flow -> (flow + item).distinctBy(UserDetailsEntity::userId) }

    override suspend fun insert(items: List<UserDetailsEntity>) =
        dbStateFlow.update { flow -> (flow + items).distinctBy(UserDetailsEntity::userId) }

    override suspend fun delete(item: UserDetailsEntity) =
        dbStateFlow.update { flow -> flow.toMutableList().apply { clear() } }

}