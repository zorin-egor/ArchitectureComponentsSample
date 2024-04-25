package com.sample.architecturecomponents.core.data.repositories.repositories

import com.sample.architecturecomponents.core.model.Repository
import kotlinx.coroutines.flow.Flow

interface RepositoriesRepository {

    fun getRepositoriesByName(name: String, page: Long, limit: Long = 30): Flow<List<Repository>>

    suspend fun add(item: Repository)

    suspend fun remove(item: Repository)

}