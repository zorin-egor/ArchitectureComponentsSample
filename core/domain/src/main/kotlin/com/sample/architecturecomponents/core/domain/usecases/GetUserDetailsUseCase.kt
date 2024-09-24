package com.sample.architecturecomponents.core.domain.usecases

import com.sample.architecturecomponents.core.common.result.Result
import com.sample.architecturecomponents.core.data.repositories.user_details.UserDetailsRepository
import com.sample.architecturecomponents.core.di.Dispatcher
import com.sample.architecturecomponents.core.di.Dispatchers
import com.sample.architecturecomponents.core.model.UserDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetUserDetailsUseCase @Inject constructor(
    private val userDetailsRepository: UserDetailsRepository,
    @Dispatcher(Dispatchers.IO) val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(userId: Long, url: String): Flow<Result<UserDetails>> =
        userDetailsRepository.getDetails(userId = userId, url = url)
            .flowOn(dispatcher)
}
