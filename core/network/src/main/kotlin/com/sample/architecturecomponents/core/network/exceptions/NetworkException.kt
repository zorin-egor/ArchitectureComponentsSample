package com.sample.architecturecomponents.core.network.exceptions

open class NetworkException(
    val errorCode: Int = Int.MIN_VALUE,
    private val errorDesc: String? = null
) : Exception(errorDesc)
