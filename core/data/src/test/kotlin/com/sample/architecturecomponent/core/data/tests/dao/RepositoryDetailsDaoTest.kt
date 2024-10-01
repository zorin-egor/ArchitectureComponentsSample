package com.sample.architecturecomponent.core.data.tests.dao

import com.sample.architecturecomponents.core.database.dao.RepositoryDetailsDao
import com.sample.architecturecomponents.core.database.model.RepositoryDetailsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class RepositoryDetailsDaoTest : RepositoryDetailsDao{

    private val dbStateFlow = MutableStateFlow(emptyList<RepositoryDetailsEntity>())

    override fun getDetailsByOwnerAndName(owner: String, name: String): Flow<RepositoryDetailsEntity?> =
        dbStateFlow.map { flow ->
            flow.filter { it.owner == owner && it.name == name }.let {
                if (it.size > 1) throw IllegalStateException("More then 1 matches")
                it.firstOrNull()
            }
        }

    override suspend fun insert(item: RepositoryDetailsEntity) =
        dbStateFlow.update { flow -> (flow + item).distinctBy(RepositoryDetailsEntity::repoId) }

    override suspend fun insert(items: List<RepositoryDetailsEntity>) =
        dbStateFlow.update { flow -> (flow + items).distinctBy(RepositoryDetailsEntity::repoId) }

    override suspend fun delete(item: RepositoryDetailsEntity) =
        dbStateFlow.update { flow -> flow.toMutableList().apply { clear() } }

}