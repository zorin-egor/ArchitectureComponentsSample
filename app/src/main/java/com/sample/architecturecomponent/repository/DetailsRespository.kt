package com.sample.architecturecomponent.repository


import com.sample.architecturecomponent.api.*
import com.sample.architecturecomponent.db.DetailsDao
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.model.Details
import com.sample.architecturecomponent.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response


class DetailsRepository(
    private val retrofitTool: RetrofitTool<Api>,
    private val detailsDao: DetailsDao
) {

    fun getDetails(item: User): Flow<ApiResponse<Details>> {
        return flow<ApiResponse<Details>> {
            // Network data fetch
            val requestResponse = try {
                item.url?.let { url ->
                    ApiSuccessResponse(retrofitTool.getApi().getDetails(url))
                } ?: IllegalStateException("User url must be set")
            } catch (e: Exception) {
                ApiErrorResponse<Void>(e.message)
            }

            val responseData = (requestResponse as? ApiSuccessResponse<Response<Details>>)?.value
            val responseItem = responseData?.body()
            if (responseData?.isSuccessful == true && responseItem != null) {
                detailsDao.insert(responseItem)
                emit(ApiSuccessResponse(responseItem))
                return@flow
            }

            // Database fetch
            val databaseResponse = try {
                item.userId?.let { id ->
                    ApiSuccessResponse(detailsDao.getDetailsById(id))
                } ?: IllegalStateException("User id must be set")
            } catch (e: Exception) {
                ApiErrorResponse<Void>(e.message)
            }

            val databaseData = (databaseResponse as? ApiSuccessResponse<Details>)?.value
            if (databaseData != null) {
                emit(ApiDatabaseResponse(databaseData))
                return@flow
            }

            // Error
            emit(ApiErrorResponse(
                (requestResponse as? ApiSuccessResponse<Response<*>>)?.value?.errorBody()?.string()
                    ?: (requestResponse as? ApiErrorResponse<*>)?.error
                    ?: (databaseResponse as? ApiErrorResponse<*>)?.error
            ))
        }
        .catch {
            emit(ApiErrorResponse(it.message))
        }
        .flowOn(Dispatchers.IO)
    }

}
