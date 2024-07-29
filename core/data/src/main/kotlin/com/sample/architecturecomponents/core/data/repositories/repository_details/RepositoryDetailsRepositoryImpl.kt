package com.sample.architecturecomponents.core.data.repositories.repository_details

import com.sample.architecturecomponents.core.data.models.toRepositoryDetailsEntity
import com.sample.architecturecomponents.core.data.models.toRepositoryDetailsModel
import com.sample.architecturecomponents.core.database.dao.RepositoriesDao
import com.sample.architecturecomponents.core.database.dao.RepositoryDetailsDao
import com.sample.architecturecomponents.core.database.model.asExternalModels
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.di.IoScope
import com.sample.architecturecomponents.core.network.ext.getResultOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class RepositoryDetailsRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val repositoryDetailsDao: RepositoryDetailsDao,
    private val repositoriesDao: RepositoriesDao,
    @IoScope private val ioScope: CoroutineScope
) : RepositoryDetailsRepository {

    override fun getDetails(owner: String, repo: String): Flow<RepositoryDetails> {
        return flow<RepositoryDetails> {
            Timber.d("getDetails($owner, $repo)")

            Timber.d("getDetails() - db")
            var dbRepositoryDetails: RepositoryDetails? = null
            repositoryDetailsDao.getDetailsByOwnerAndName(owner = owner, name = repo)
                .take(1)
                .filterNotNull()
                .map { it.asExternalModels() }
                .catch { Timber.e(it) }
                .onEach { dbRepositoryDetails = it }
                .collect(::emit)

            Timber.d("getDetails() - network request")
            val result = networkDatasource.getRepositoryDetails(owner = owner, repo = repo)
                .getResultOrThrow().toRepositoryDetailsModel()

            if (dbRepositoryDetails != null && dbRepositoryDetails == result) {
                Timber.d("getDetails() - db == network")
                return@flow
            }

            emit(result)

            ioScope.launch {
                runCatching { repositoryDetailsDao.insert(result.toRepositoryDetailsEntity()) }
                    .exceptionOrNull()?.let(Timber::e)
            }

            Timber.d("getDetails() - end")
        }
    }

    override suspend fun insert(item: RepositoryDetails) = repositoryDetailsDao.insert(item.toRepositoryDetailsEntity())

    override suspend fun delete(item: RepositoryDetails) = repositoryDetailsDao.delete(item.toRepositoryDetailsEntity())

}
