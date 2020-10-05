package com.sample.architecturecomponent.api


abstract sealed class ApiResponse<T>

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val value: T) : ApiResponse<T>()

data class ApiDatabaseResponse<T>(val value: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val error: String? = null) : ApiResponse<T>()
