package com.sample.architecturecomponents.core.domain.usecases

import com.sample.architecturecomponents.core.data.repositories.details.DetailsRepository
import com.sample.architecturecomponents.core.model.Details
import com.sample.architecturecomponents.core.network.Dispatcher
import com.sample.architecturecomponents.core.network.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetDetailsUseCase @Inject constructor(
    private val detailsRepository: DetailsRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(userId: Long, url: String): Flow<Details> =
        detailsRepository.getDetails(userId = userId, url = url)
            .flowOn(dispatcher)
}
