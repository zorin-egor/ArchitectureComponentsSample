package com.sample.architecturecomponents.core.ui.ext

import android.content.Context
import com.sample.architecturecomponents.core.network.exceptions.EmptyException
import com.sample.architecturecomponents.core.network.exceptions.NetworkException
import com.sample.architecturecomponents.core.ui.R
import java.net.HttpURLConnection
import java.net.UnknownHostException

val NETWORK_ERRORS = mapOf(
    HttpURLConnection.HTTP_BAD_REQUEST to R.string.error_network_400,
    HttpURLConnection.HTTP_UNAUTHORIZED to R.string.error_network_401,
    HttpURLConnection.HTTP_FORBIDDEN to R.string.error_network_403,
    HttpURLConnection.HTTP_NOT_FOUND to R.string.error_network_404,
    HttpURLConnection.HTTP_BAD_METHOD to R.string.error_network_405,
    HttpURLConnection.HTTP_CLIENT_TIMEOUT to R.string.error_network_408,
    HttpURLConnection.HTTP_CONFLICT to R.string.error_network_409,
    HttpURLConnection.HTTP_UNSUPPORTED_TYPE to R.string.error_network_415,
    HttpURLConnection.HTTP_INTERNAL_ERROR to R.string.error_network_500,
)

fun Context.getErrorMessage(error: Throwable): String {
    return when(error) {
        EmptyException -> getString(R.string.error_network_empty)
        is UnknownHostException -> getString(R.string.error_network_no_internet)
        is NetworkException -> NETWORK_ERRORS[error.errorCode]
            ?.let { runCatching { getString(it) }}
            ?.getOrNull()
        else -> error.message
    } ?: getString(R.string.error_unknown)
}