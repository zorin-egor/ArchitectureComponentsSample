package com.sample.architecturecomponent.core.data.tests.dao

import com.sample.architecturecomponents.core.common.extensions.safeSubList
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.model.RepositoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class RepositoriesDaoTestImpl : RepositoriesDao {

    private val dbStateFlow = MutableStateFlow(emptyList<RepositoryEntity>())

    override fun getRepos(): Flow<List<RepositoryEntity>> = dbStateFlow

    override fun getRepositoriesByName(name: String, offset: Long, limit: Long): Flow<List<RepositoryEntity>> =
        dbStateFlow.map { flow ->
            flow.filter { it.name.contains(name) }
                .safeSubList(offset.toInt(), (offset + limit).toInt())
        }

    override suspend fun insert(item: RepositoryEntity) =
        dbStateFlow.update { flow -> (flow + item).distinctBy(RepositoryEntity::repoId) }

    override suspend fun insert(items: List<RepositoryEntity>) =
        dbStateFlow.update { flow -> (flow + items).distinctBy(RepositoryEntity::repoId) }

    override suspend fun delete(item: RepositoryEntity) =
        dbStateFlow.update { flow -> flow.toMutableList().apply { remove(item) } }

    override suspend fun delete() =
        dbStateFlow.update { flow -> flow.toMutableList().apply { clear() } }

}