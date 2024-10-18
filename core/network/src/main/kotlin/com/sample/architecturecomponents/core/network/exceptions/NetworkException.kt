package com.sample.architecturecomponents.core.network.exceptions

import java.io.IOException

open class NetworkException(
    val errorCode: Int = Int.MIN_VALUE,
    private val errorDesc: String? = null
) : IOException(errorDesc)
