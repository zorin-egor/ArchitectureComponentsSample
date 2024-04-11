package com.sample.architecturecomponents.core.data.repositories.details

import com.sample.architecturecomponents.core.model.Details
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {

    fun getDetails(userId: Long, url: String): Flow<Details>

}