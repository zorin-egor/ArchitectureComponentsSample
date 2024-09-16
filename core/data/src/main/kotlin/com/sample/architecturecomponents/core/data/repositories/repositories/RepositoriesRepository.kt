package com.sample.architecturecomponents.core.data.repositories.repositories

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.model.Repository
import kotlinx.coroutines.flow.Flow

interface RepositoriesRepository {

    fun getRepositoriesByName(name: String, page: Long, limit: Long = 30): Flow<Result<List<Repository>>>

    suspend fun insert(item: Repository)

    suspend fun delete(item: Repository)

}