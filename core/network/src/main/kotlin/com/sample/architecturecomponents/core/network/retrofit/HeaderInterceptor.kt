package com.sample.architecturecomponents.core.network.retrofit

import android.net.Uri
import com.sample.architecturecomponents.core.datastore.SettingsPreference
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import timber.log.Timber

class HeaderInterceptor(
    private val preference: SettingsPreference,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val baseUrl = runBlocking { preference.getBaseUrl() }
        val requestHost = chain.request().url.host
        val baseHost = Uri.parse(baseUrl).host
        if (requestHost != baseHost) {
            return chain.proceed(chain.request())
        }

        val token = runBlocking { preference.getAuthToken() }
            ?: return chain.proceed(chain.request())

        Timber.d("intercept() - token: $token")

        val newRequest = chain.request().newBuilder()
            .addHeader("some_auth_header", token)

        return chain.proceed(newRequest.build())
    }

}