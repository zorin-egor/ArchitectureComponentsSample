package com.sample.architecturecomponent.models

import com.sample.architecturecomponent.exceptions.ConnectionException
import java.io.IOException
import java.net.UnknownHostException

sealed class Container<out T>

object Empty : Container<Nothing>()

data class Data<T>(val value: T) : Container<T>()

data class Error(val type: ErrorType = ErrorType.Unknown) : Container<Nothing>()


sealed class ErrorType {

    class Error(val message: String) : ErrorType()

    class Unhandled(val error: Throwable) : ErrorType()

    object Unknown : ErrorType()

    object IOConnection : ErrorType()

    object UnknownHost : ErrorType()

    object Connection : ErrorType()

}

inline fun <T> action(action: () -> T?): Container<T> {
    return try {
        action()?.let { Data(it) } ?: Empty
    } catch (e: UnknownHostException) {
        Error(ErrorType.UnknownHost)
    } catch (e: ConnectionException) {
        Error(ErrorType.Connection)
    } catch (e: IOException) {
        Error(ErrorType.IOConnection)
    } catch (e: Exception) {
        Error(ErrorType.Unhandled(e))
    }
}