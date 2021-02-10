package com.sample.architecturecomponent.api

import com.sample.architecturecomponent.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {

    companion object {
        const val BASE_URL: String = "https://api.github.com/"
        const val DEFAULT_SINCE_ID = 0L
    }

    @GET("users")
    suspend fun getUsers(@Query("since") since: Long): Response<List<User>>

    @GET
    suspend fun getDetails(@Url url: String): Response<Details>

}

fun <T> mapTo(value: Response<T>): Container<T> {
    return when {
        value.isSuccessful -> value.body()?.let { Data(it) } ?: Empty
        else -> {
            Error(
                value.errorBody()?.string()
                        ?.let { ErrorType.Error(it) }
                        ?: ErrorType.Unknown
            )
        }
    }
}

fun <T> mapTo(value: Container<Response<T>>): Container<T> {
    return when(value) {
        is Data -> value.value.let(::mapTo)
        is Error -> Error(value.type)
        is Empty -> Error(ErrorType.Unknown)
    }
}