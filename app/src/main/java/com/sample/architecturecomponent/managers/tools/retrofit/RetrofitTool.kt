package com.sample.architecturecomponent.managers.tools

import android.annotation.SuppressLint
import com.google.gson.GsonBuilder
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class RetrofitTool<T>(
    private val url: String,
    private val clazz: Class<T>
) {

    companion object {
        const val OUTPUT_DATA_PATTERN_DEFAULT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ"
        const val READ_TIMEOUT = 60L
        const val WRITE_TIMEOUT = 60L
        const val CONNECT_TIMEOUT = 60L

        private val gsonConverterFactory: GsonBuilder
            get() = GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setLenient()
                    .setPrettyPrinting()
                    .setDateFormat(OUTPUT_DATA_PATTERN_DEFAULT)

        private val okHttp: OkHttpClient.Builder
            get() = OkHttpClient.Builder()
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)

        private val unsafeOkHttp: OkHttpClient.Builder
            get() {
                return okHttp.sslSocketFactory(getSocketFactory(unsafeX509TrustManager), unsafeX509TrustManager)
                        .hostnameVerifier(hostnameVerifier)
            }

        private val connectionSpec: List<ConnectionSpec>
            get() {
                val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .cipherSuites(
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                        )
                        .build()
                return listOf(spec)
            }

        private val unsafeX509TrustManager: X509TrustManager
            get() = object : X509TrustManager {

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            }

        private val hostnameVerifier = HostnameVerifier { hostname, session ->
            HttpsURLConnection.getDefaultHostnameVerifier().run {
                verify(hostname, session)
            }
        }

        private fun getSocketFactory(trustManager: TrustManager): SSLSocketFactory {
            val trustAllCerts = arrayOf(trustManager)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            return sslContext.socketFactory
        }
    }

    var isSslOn: Boolean = true
        private set

    var isCiphers: Boolean = false
        private set

    var okHttpBuilder: OkHttpClient.Builder = okHttp
    var datePattern: String = OUTPUT_DATA_PATTERN_DEFAULT
    var gsonBuilder: GsonBuilder = gsonConverterFactory.setDateFormat(datePattern)

    private var api: T? = null

    private fun retrofitBuilder(okHttpBuilder: OkHttpClient.Builder): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(okHttpBuilder.build())
    }

    private fun buildApi(): T {
        return retrofitBuilder(when {
            !isSslOn -> {
                okHttpBuilder.sslSocketFactory(getSocketFactory(unsafeX509TrustManager), unsafeX509TrustManager)
                    .hostnameVerifier(hostnameVerifier)
            }
            isCiphers -> {
                okHttpBuilder.connectionSpecs(connectionSpec)
            }
            else -> {
                okHttpBuilder
            }
        }).baseUrl(url).build()
            .create(clazz)
            .also { api = it }
    }

    fun getApi(): T {
        return api ?: buildApi()
    }
}

fun <T> Response<T>.isCache(): Boolean {
    return raw().cacheResponse != null
}