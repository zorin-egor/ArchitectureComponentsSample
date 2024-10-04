package com.sample.architecturecomponents.core.data.repositories.repositories

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.common.result.combineLeftFirst
import com.sample.architecturecomponents.core.common.result.startLoading
import com.sample.architecturecomponents.core.data.models.toRepositoryEntities
import com.sample.architecturecomponents.core.data.models.toRepositoryEntity
import com.sample.architecturecomponents.core.data.models.toRepositoryModels
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.model.RepositoryEntity
import com.sample.architecturecomponents.core.database.model.asExternalModels
import com.sample.architecturecomponents.core.di.IoScope
import com.sample.architecturecomponents.core.model.Repository
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.ext.REPOSITORY_SORT_BY_NAME
import com.sample.architecturecomponents.core.network.ext.getResultOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


internal class RepositoriesRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val repositoriesDao: RepositoriesDao,
    @IoScope private val ioScope: CoroutineScope
) : RepositoriesRepository {

    override fun getRepositoriesByName(name: String, page: Long, limit: Long, isDescSort: Boolean): Flow<Result<List<Repository>>> {
        val dbOffset = (page - 1) * limit
        val dbFlow = repositoriesDao.getRepositoriesByName(name = name, offset = dbOffset, limit = limit)
            .take(1)
            .filterNot { it.isEmpty() }
            .map<List<RepositoryEntity>, Result<List<Repository>>> { Result.Success(it.asExternalModels()) }
            .onStart { emit(Result.Loading) }
            .catch {
                Timber.e(it)
                emit(Result.Error(it))
            }

        val networkFlow = flow {
            emit(Result.Loading)

            val response = networkDatasource.getRepositories(
                name = name,
                page = page.toInt(),
                perPage = limit.toInt(),
                sort = REPOSITORY_SORT_BY_NAME,
                isDescOrder = isDescSort
            ).getResultOrThrow().networkRepositories

            if (response.isNotEmpty()) {
                ioScope.launch {
                    runCatching { repositoriesDao.insert(response.toRepositoryEntities()) }
                        .onFailure(Timber::e)
                }
            }

            emit(Result.Success(response.toRepositoryModels()))
        }.catch {
            Timber.e(it)
            emit(Result.Error(it))
        }

        return networkFlow.combineLeftFirst(dbFlow)
            .startLoading()
            .distinctUntilChanged()
    }

    override suspend fun insert(item: Repository) = repositoriesDao.insert(item.toRepositoryEntity())

    override suspend fun delete(item: Repository) = repositoriesDao.delete(item.toRepositoryEntity())

}
