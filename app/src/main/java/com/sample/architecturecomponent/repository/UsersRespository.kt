package com.sample.architecturecomponent.repository

import android.content.Context
import com.sample.architecturecomponent.api.*
import com.sample.architecturecomponent.db.UsersDao
import com.sample.architecturecomponent.managers.tools.RetrofitTool
import com.sample.architecturecomponent.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.*


class UsersRepository(
    private val context: Context,
    private val retrofitTool: RetrofitTool<Api>,
    private val usersDao: UsersDao
) : BaseRepository() {

    companion object {
        val TAG = UsersRepository::class.java.simpleName

        private const val DEFAULT_USER_ID = 0L
    }

    private var since: Long = DEFAULT_USER_ID
    private val users: TreeSet<User> = TreeSet()

    fun getUsers(sinceId: Long = DEFAULT_USER_ID): Flow<ApiResponse<List<User>>> {
        return flow<ApiResponse<List<User>>> {
            withContext(Dispatchers.IO) {
                if (sinceId == DEFAULT_USER_ID) {
                    users.clear()
                }

                val databaseAsync = async {
                    try {
                        ApiSuccessResponse(usersDao.getUsersPage(since))
                    } catch (e: Exception) {
                        ApiErrorResponse(e.message)
                    }
                }

                val requestAsync = async {
                    try {
                        ApiSuccessResponse(retrofitTool.getApi().getUsers(sinceId))
                    } catch (e: Exception) {
                        ApiErrorResponse(e.message)
                    }
                }

                val databaseResponse = databaseAsync.await()
                val databaseSuccess = databaseResponse as? ApiSuccessResponse
                if (databaseSuccess?.value?.isNotEmpty() == true) {
                    users.addAll(databaseResponse.value)
                    since += databaseSuccess.value.size
                }

                val requestResponse = requestAsync.await()
                val requestSuccess = requestResponse as? ApiSuccessResponse
                val dataResponse = requestSuccess?.value?.body()
                val result = if (requestSuccess?.value?.isSuccessful == true && dataResponse != null) {
                    since = dataResponse.last().id

                    val items = users.run {
                        addAll(dataResponse)
                        toList()
                    }

                    usersDao.insertAll(items)
                    ApiSuccessResponse(items)
                } else {
                    when {
                        databaseSuccess != null -> {
                            ApiDatabaseResponse(users.toList())
                        }
                        else -> {
                            ApiErrorResponse(
                                (requestResponse as? ApiErrorResponse<*>)?.error
                                    ?: (requestAsync as? ApiErrorResponse<*>)?.error
                            )
                        }
                    }
                }

                return@withContext result
            }.also {
                emit(it)
            }
        }
        .catch {
            emit(ApiErrorResponse(it.message))
        }
        .flowOn(Dispatchers.IO)
    }

    fun getNextUsers(): Flow<ApiResponse<List<User>>> {
        return getUsers(since)
    }

    fun addUser(item: User): Flow<ApiResponse<List<User>>> {
        return flow<ApiResponse<List<User>>> {
            usersDao.insert(item)
            val result = users.run {
                add(item)
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
