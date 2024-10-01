package com.sample.architecturecomponents.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable, val error: String? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> = map<T, Result<T>> { Result.Success(it) }
    .onStart { emit(Result.Loading) }
    .catch { emit(Result.Error(it)) }

fun <T> kotlin.Result<T>.toResult(): Result<T> {
    return when {
        isSuccess -> Result.Success(getOrThrow())
        isFailure -> Result.Error(exceptionOrNull() ?: Exception("Unknown error"))
        else -> error("Wrong result state")
    }
}

fun <T> Flow<Result<T>>.startLoading(): Flow<Result<T>> = filterNot { it is Result.Loading }
    .onStart { emit(Result.Loading) }

fun <T> Flow<Result<T>>.combineLeftFirst(flow: Flow<Result<T>>): Flow<Result<T>> =
    combine(this, flow) { first, second ->
        when {
            first is Result.Success -> first
            second is Result.Success -> second
            else -> first
        }
    }

