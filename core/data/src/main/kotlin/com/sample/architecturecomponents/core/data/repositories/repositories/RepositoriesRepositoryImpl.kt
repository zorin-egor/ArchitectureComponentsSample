package com.sample.architecturecomponents.core.data.repositories.repositories

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.common.result.asResult
import com.sample.architecturecomponents.core.data.models.toRepositoryEntities
import com.sample.architecturecomponents.core.data.models.toRepositoryEntity
import com.sample.architecturecomponents.core.data.models.toRepositoryModels
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.model.asExternalModel
import com.sample.architecturecomponents.core.model.Repository
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.di.IoScope
import com.sample.architecturecomponents.core.network.ext.REPOSITORY_SORT_BY_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class RepositoriesRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val repositoriesDao: RepositoriesDao,
    @IoScope private val ioScope: CoroutineScope
) : RepositoriesRepository {

    override fun getRepositoriesByName(name: String, page: Long, limit: Long): Flow<Result<List<Repository>>> {
        return flow<List<Repository>> {
            Timber.d("getRepositories($name)")

            Timber.d("getRepositories() - network request")
            val response = kotlin.runCatching {
                networkDatasource.getRepositories(name = name, page = page.toInt(),
                    perPage = limit.toInt(), sort = REPOSITORY_SORT_BY_NAME, isDescOrder = false)
                        .body()
            }

            val result = response.getOrNull()?.networkRepositories
            if (result?.isNotEmpty() == true) {
                Timber.d("getRepositories() - db == network")
                emit(result.toRepositoryModels())
                ioScope.launch {
                    runCatching { repositoriesDao.insert(result.toRepositoryEntities()) }
                        .exceptionOrNull()?.let(Timber::e)
                }
                return@flow
            }

            response.exceptionOrNull()?.let(Timber::e)

            Timber.d("getRepositories() - db")
            val offset = (page - 1) * limit
            repositoriesDao.getRepositoriesByName(name = name, offset = offset, limit = limit)
                .take(1)
                .mapNotNull {
                    it.takeIf { it.isNotEmpty() }
                        ?.asExternalModel()
                }
                .catch { Timber.e(it) }
                .collect(::emit)

            Timber.d("getRepositories() - end")
        }.asResult()
    }

    override suspend fun insert(item: Repository) = repositoriesDao.insert(item.toRepositoryEntity())

    override suspend fun delete(item: Repository) = repositoriesDao.delete(item.toRepositoryEntity())

}
