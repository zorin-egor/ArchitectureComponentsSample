package com.sample.architecturecomponents.core.network.ext

import com.sample.architecturecomponents.core.network.exceptions.EmptyException
import com.sample.architecturecomponents.core.network.exceptions.NetworkException
import com.sample.architecturecomponents.core.network.models.NetworkError
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection

fun ResponseBody.getError(): String? {
    val errorStr = runCatching { string() }.getOrNull()
    return errorStr?.jsonToObject<NetworkError>()?.error ?: errorStr
}

fun <T> Response<T>.getResultOrThrow(): T {
    return when(val code = code()) {
        HttpURLConnection.HTTP_OK -> body() ?: throw EmptyException
        else -> throw NetworkException(errorCode = code, errorDesc = errorBody()?.getError())
    }
}