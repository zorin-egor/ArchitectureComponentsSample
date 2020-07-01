package com.sample.architecturecomponent.managers.exceptions


import java.io.IOException

class ConnectionException : IOException {

    constructor() : super()

    constructor(cause: Throwable) : super(cause)

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

}