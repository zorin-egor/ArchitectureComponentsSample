package com.sample.architecturecomponents.core.network.retrofit

import com.sample.architecturecomponents.core.datastore.SettingsPreference
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import timber.log.Timber

class HeaderInterceptor(private val preference: SettingsPreference) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val token = runBlocking { preference.getAuthToken() }
            ?: return chain.proceed(chain.request())

        Timber.d("intercept() - token: $token")

        val newRequest = chain.request().newBuilder()
            .addHeader("some_auth_header", token)

        return chain.proceed(newRequest.build())
    }

}