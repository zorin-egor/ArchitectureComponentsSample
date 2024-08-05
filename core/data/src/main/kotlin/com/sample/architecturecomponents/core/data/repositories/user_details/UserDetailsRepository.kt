package com.sample.architecturecomponents.core.data.repositories.user_details

import com.sample.architecturecomponents.core.model.UserDetails
import kotlinx.coroutines.flow.Flow

interface UserDetailsRepository {

    fun getDetails(userId: Long, url: String): Flow<Result<UserDetails>>

}