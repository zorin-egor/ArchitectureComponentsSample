package com.sample.architecturecomponents.core.domain.usecases

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.repository_details.RepositoryDetailsRepository
import com.sample.architecturecomponents.core.di.Dispatcher
import com.sample.architecturecomponents.core.di.Dispatchers
import com.sample.architecturecomponents.core.model.RepositoryDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetRepositoryDetailsByOwnerUseCase @Inject constructor(
    private val repositoryDetailsRepository: RepositoryDetailsRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(owner: String, repo: String): Flow<Result<RepositoryDetails>> =
        repositoryDetailsRepository.getDetails(owner = owner, repo = repo)
            .flowOn(dispatcher)
}
