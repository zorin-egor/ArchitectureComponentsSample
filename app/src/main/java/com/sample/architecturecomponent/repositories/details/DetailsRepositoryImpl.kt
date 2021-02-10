package com.sample.architecturecomponent.repositories.details

import com.sample.architecturecomponent.api.*
import com.sample.architecturecomponent.db.DetailsDao
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class DetailsRepositoryImpl(
    private val retrofitTool: RetrofitTool<Api>,
    private val detailsDao: DetailsDao
) : DetailsRepository {

    override fun getDetails(user: User): Flow<Container<Details>> {
        return flow<Container<Details>> {

            detailsDao.getDetailsById(user.userId)
                .catch {
                    emit(Error(ErrorType.Unhandled(it)))
                }.collect { item ->
                    if (item != null) {
                        emit(Data(item))
                    }

                    if (item != null && !item.isUpdateTime) {
                        return@collect
                    }

                    val url = user.url ?: throw IllegalArgumentException("User has no detail link")

                    val response = action { retrofitTool.getApi().getDetails(url) }
                        .let(::mapTo)

                    when (response) {
                        is Data -> {
                            detailsDao.insert(response.value.apply {
                                updateTime = System.currentTimeMillis()
                            })
                        }
                        else -> {
                            emit(response)
                        }
                    }
                }

        }.catch {
            emit(Error(ErrorType.Unhandled(it)))
        }.flowOn(Dispatchers.IO)
    }

}
