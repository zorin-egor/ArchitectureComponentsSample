package com.sample.architecturecomponents.core.data.repositories.repositories

import com.sample.architecturecomponents.core.data.models.toExternalModel
import com.sample.architecturecomponents.core.data.models.toRepoEntity
import com.sample.architecturecomponents.core.data.models.toRepositoryEntity
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.model.asExternalModel
import com.sample.architecturecomponents.core.model.Repository
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.di.IoScope
import com.sample.architecturecomponents.core.network.ext.getResultOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class RepositoriesRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val repositoriesDao: RepositoriesDao,
    @IoScope private val ioScope: CoroutineScope
) : RepositoriesRepository {

    override fun getRepositoriesByName(name: String, page: Long, limit: Long): Flow<List<Repository>> {
        return flow<List<Repository>> {
            Timber.d("getRepos($name)")

            Timber.d("getRepos() - db")
            val dbItems = mutableListOf<Repository>()
            repositoriesDao.getRepoByName(name = name, offset = page, limit = limit)
                .take(1)
                .catch { Timber.e(it) }
                .mapNotNull { it.asExternalModel() }
                .onEach(dbItems::addAll)
                .collect(::emit)

            Timber.d("getRepos() - network request")
            val response = networkDatasource.getRepositories(name = name, page = page.toInt(), perPage = limit.toInt())
                .getResultOrThrow()

            val result = response.networkItems.toExternalModel()

            if (dbItems.isNotEmpty() && dbItems == result) {
                Timber.d("getRepos() - db == network")
                return@flow
            }

            emit(result)

            ioScope.launch {
                runCatching { repositoriesDao.insertAll(response.networkItems.toRepositoryEntity()) }
                    .exceptionOrNull()?.let(Timber::e)
            }

            Timber.d("getRepos() - end")
        }
    }

    override suspend fun add(item: Repository) = repositoriesDao.insert(item.toRepoEntity())

    override suspend fun remove(item: Repository) = repositoriesDao.delete(item.toRepoEntity())

}
