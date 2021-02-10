package com.sample.architecturecomponent.managers.tools.retrofit

import android.content.Context
import com.sample.architecturecomponent.exceptions.ConnectionException
import com.sample.architecturecomponent.managers.extensions.isOnline
import okhttp3.Interceptor

class ConnectionInterceptor(private val context: Context) : Interceptor {

    @Throws(ConnectionException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        if (!context.isOnline()) {
            throw ConnectionException("No connection")
        }
        return chain.proceed(chain.request())
    }

}