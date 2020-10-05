package com.sample.architecturecomponent.repository

import com.sample.architecturecomponent.api.*
import com.sample.architecturecomponent.db.UsersDao
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response


class UsersRepository(
    private val retrofitTool: RetrofitTool<Api>,
    private val usersDao: UsersDao
) : BaseRepository() {

    companion object {
        val TAG = UsersRepository::class.java.simpleName

        private const val DEFAULT_USER_ID = "0"
    }

    private var since: String = DEFAULT_USER_ID
    private val users: MutableList<User> = arrayListOf()

    fun getUsers(sinceId: String = DEFAULT_USER_ID): Flow<ApiResponse<List<User>>> {
        return flow<ApiResponse<List<User>>> {
            // Reset list
            if (sinceId == DEFAULT_USER_ID) {
                users.clear()
            }

            // Network data fetch
            val requestResponse = try {
                ApiSuccessResponse(retrofitTool.getApi().getUsers(sinceId))
            } catch (e: Exception) {
                ApiErrorResponse(e.message)
            }

            val responseData = (requestResponse as? ApiSuccessResponse)?.value
            val responseItems = responseData?.body()
            if (responseData?.isSuccessful == true && responseItems != null) {
                since = responseItems.last().userId ?: DEFAULT_USER_ID

                val items = users.run {
                    addAll(responseItems)
                    toList()
                }

                usersDao.insertAll(responseItems)
                emit(ApiSuccessResponse(items))
                return@flow
            }

            // Database fetch
            val databaseResponse = try {
                ApiSuccessResponse(usersDao.getUsersPage(users.size))
            } catch (e: Exception) {
                ApiErrorResponse(e.message)
            }

            val databaseData = (databaseResponse as? ApiSuccessResponse)?.value
            if (databaseData?.isNotEmpty() == true) {
                since = databaseData.last().userId ?: DEFAULT_USER_ID

                val items = users.run {
                    addAll(databaseData)
                    toList()
                }

                emit(ApiDatabaseResponse(items))
                return@flow
            }

            // Error
            emit(ApiErrorResponse(
                (requestResponse as? ApiSuccessResponse<Response<*>>)?.value?.errorBody()?.string()
                    ?: (requestResponse as? ApiErrorResponse<*>)?.error
                    ?: (databaseData as? ApiErrorResponse<*>)?.error
            ))
        }
        .catch {
            emit(ApiErrorResponse(it.message))
        }
        .flowOn(Dispatchers.IO)
    }

    fun getNextUsers(): Flow<ApiResponse<List<User>>> {
        return getUsers(since)
    }

    fun addUser(index: Int, item: User): Flow<ApiResponse<List<User>>> {
        return flow<ApiResponse<List<User>>> {
            usersDao.insert(item)
            val result = users.run {
                add(index, item)
                toList()
            }
            emit(ApiSuccessResponse(result))
        }
        .catch {
            emit(ApiErrorResponse(it.message))
        }
        .flowOn(Dispatchers.IO)
    }

    fun removeUser(item: User): Flow<ApiResponse<List<User>>> {
        return flow<ApiResponse<List<User>>> {
            users.remove(item)
            usersDao.delete(item)
            emit(ApiSuccessResponse(users.toList()))
        }
        .catch {
            emit(ApiErrorResponse(it.message))
        }
        .flowOn(Dispatchers.IO)
    }

}
