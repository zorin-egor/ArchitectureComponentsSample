package com.sample.architecturecomponent.managers.tools

import android.content.Context
import com.google.gson.GsonBuilder
import com.sample.architecturecomponent.managers.exceptions.ConnectionException
import com.sample.architecturecomponent.managers.extensions.isOnline
import okhttp3.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


class RetrofitTool<T> {

    companion object {

        val TAG = RetrofitTool::class.java.simpleName

        const val OUTPUT_DATA_PATTERN_DEFAULT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ"

        const val READ_TIMEOUT = 60L
        const val WRITE_TIMEOUT = 60L
        const val CONNECT_TIMEOUT = 60L

        val gsonConverterFactory: GsonBuilder
            get() = GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setLenient()
                    .setPrettyPrinting()
                    .setDateFormat(OUTPUT_DATA_PATTERN_DEFAULT)

        val okHttp: OkHttpClient.Builder
            get() = OkHttpClient.Builder()
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)

        val unsafeOkHttp: OkHttpClient.Builder
            get() {
                return okHttp.sslSocketFactory(getSocketFactory(unsafeX509TrustManager), unsafeX509TrustManager)
                        .hostnameVerifier(hostnameVerifier)
            }

        val connectionSpec: List<ConnectionSpec>
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

        val unsafeX509TrustManager: X509TrustManager
            get() = object : X509TrustManager {

                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            }

        val hostnameVerifier = HostnameVerifier { hostname, session ->
            HttpsURLConnection.getDefaultHostnameVerifier().run {
                verify(hostname, session)
            }
        }

        fun getSocketFactory(trustManager: X509TrustManager): SSLSocketFactory {
            val trustAllCerts = arrayOf<TrustManager>(trustManager)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            return sslContext.socketFactory
        }
    }

    var datePattern: String = OUTPUT_DATA_PATTERN_DEFAULT
    var gsonBuilder: GsonBuilder = gsonConverterFactory.setDateFormat(datePattern)
    var okHttpBuilder: OkHttpClient.Builder = okHttp

    var isSslOn: Boolean = true
    var isCiphers: Boolean = false

    private val mContext: Context
    private val mUrl: String
    private var mClazz: Class<T>? = null

    private var mApi: T? = null
    private var mRetrofit: Retrofit? = null
    private var mBuilder: Retrofit.Builder? = null


    constructor(context: Context, url: String, clazz: Class<T>? = null) {
        mContext = context
        mUrl = url
        mClazz = clazz
        init()
    }

    private fun init() {
        okHttpBuilder.apply {
            addInterceptor(ConnectionInterceptor(mContext))
        }
    }

    private fun retrofitBuilder(okHttpBuilder: OkHttpClient.Builder): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(okHttpBuilder.build())
    }

    private fun getOkHttpSpecs(okHttpClient: OkHttpClient.Builder): OkHttpClient.Builder {
        return okHttpClient.connectionSpecs(connectionSpec)
    }

    fun buildApi(): RetrofitTool<T> {
        if (!isSslOn) {
            okHttpBuilder = unsafeOkHttp
            mBuilder = retrofitBuilder(okHttpBuilder)
        } else if (isCiphers) {
            okHttpBuilder = getOkHttpSpecs(okHttpBuilder)
            mBuilder = retrofitBuilder(okHttpBuilder)
        } else {
            mBuilder = retrofitBuilder(okHttpBuilder)
        }

        mRetrofit = mBuilder?.let { builder ->
            builder.baseUrl(mUrl).build()
        } ?: throw RuntimeException("The \"mBuilder\" field must be init before base url build...")

        mApi = mClazz?.let { clazz ->
            mRetrofit?.create(clazz)
        } ?: throw RuntimeException("The \"mClazz\" field must be init before api create...")

        return this
    }

    fun getApi(): T {
        if (mApi == null) {
            buildApi()
        }

        return mApi ?: throw RuntimeException("The \"buildApi\" function must be called before using this function...")
    }
}

fun <T> Response<T>.isCache(): Boolean {
    return raw().cacheResponse != null
}

class ConnectionInterceptor(private val context : Context) : Interceptor {

    @Throws(ConnectionException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        if (!context.isOnline()) {
            throw ConnectionException()
        }

        return chain.proceed(chain.request())
    }

}