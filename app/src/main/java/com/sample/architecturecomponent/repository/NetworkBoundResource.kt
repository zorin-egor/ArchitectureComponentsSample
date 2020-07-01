package com.sample.architecturecomponent.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor() {

    private val result = MediatorLiveData<ResultType>()

    init {

    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {

    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<ResultType>

    @WorkerThread
    protected open fun processResponse(response: RequestType) = response

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun onFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun onDatabase(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<RequestType>

}
