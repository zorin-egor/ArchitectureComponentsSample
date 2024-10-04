package com.sample.architecturecomponents.core.data.repositories.repository_details

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.common.result.combineLeftFirst
import com.sample.architecturecomponents.core.common.result.startLoading
import com.sample.architecturecomponents.core.data.models.toRepositoryDetailsEntity
import com.sample.architecturecomponents.core.data.models.toRepositoryDetailsModel
import com.sample.architecturecomponents.core.database.dao.RepositoryDetailsDao
import com.sample.architecturecomponents.core.database.model.RepositoryDetailsEntity
import com.sample.architecturecomponents.core.database.model.asExternalModels
import com.sample.architecturecomponents.core.di.IoScope
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.core.network.NetworkDataSource
import com.sample.architecturecomponents.core.network.ext.getResultOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

internal class RepositoryDetailsRepositoryImpl @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val repositoryDetailsDao: RepositoryDetailsDao,
    @IoScope private val ioScope: CoroutineScope
) : RepositoryDetailsRepository {

    override fun getDetails(owner: String, repo: String): Flow<Result<RepositoryDetails>> {
        val dbFlow = repositoryDetailsDao.getDetailsByOwnerAndName(owner = owner, name = repo)
            .take(1)
            .filterNotNull()
            .map<RepositoryDetailsEntity, Result<RepositoryDetails>> { Result.Success(it.asExternalModels()) }
            .onStart { emit(Result.Loading) }
            .catch {
                Timber.e(it)
                emit(Result.Error(it))
            }

        val networkFlow = flow {
            emit(Result.Loading)

            val response = networkDatasource.getRepositoryDetails(owner = owner, repo = repo)
                .getResultOrThrow()

            ioScope.launch {
                runCatching { repositoryDetailsDao.insert(response.toRepositoryDetailsEntity()) }
                    .onFailure(Timber::e)
            }

            emit(Result.Success(response.toRepositoryDetailsModel()))
        }.catch {
            Timber.e(it)
            emit(Result.Error(it))
        }

        return networkFlow.combineLeftFirst(dbFlow)
            .startLoading()
            .distinctUntilChanged()
    }

    override suspend fun insert(item: RepositoryDetails) = repositoryDetailsDao.insert(item.toRepositoryDetailsEntity())

    override suspend fun delete(item: RepositoryDetails) = repositoryDetailsDao.delete(item.toRepositoryDetailsEntity())

}
