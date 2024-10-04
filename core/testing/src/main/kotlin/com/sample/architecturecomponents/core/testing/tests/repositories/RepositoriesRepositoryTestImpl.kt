package com.sample.architecturecomponents.core.testing.tests.repositories

import com.sample.architecturecomponents.core.common.extensions.safeSubList
import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.repositories.RepositoriesRepository
import com.sample.architecturecomponents.core.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update


class RepositoriesRepositoryTestImpl : RepositoriesRepository {

    private val repositories = MutableStateFlow<List<Repository>>(emptyList())

    override fun getRepositoriesByName(name: String, page: Long, limit: Long, isDescSort: Boolean): Flow<Result<List<Repository>>> {
        return repositories.map<List<Repository>, Result<List<Repository>>> { flow ->
            val offset = (page - 1) * limit
            val items = flow.filter { it.name.contains(name) }
                .safeSubList(offset.toInt(), (offset + limit).toInt())
            Result.Success(items)
        }.onStart {
            emit(Result.Loading)
        }.take(2)
    }

    override suspend fun insert(item: Repository) {
        repositories.update { flow -> (flow + item).distinctBy(Repository::id) }
    }

    override suspend fun delete(item: Repository) {
        repositories.update { flow -> flow.toMutableList().apply { remove(item) } }
    }

}