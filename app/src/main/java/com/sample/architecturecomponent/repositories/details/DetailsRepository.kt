package com.sample.architecturecomponent.repositories.details

import com.sample.architecturecomponent.models.Container
import com.sample.architecturecomponent.models.Details
import com.sample.architecturecomponent.models.User
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {

    fun getDetails(user: User): Flow<Container<Details>>

}