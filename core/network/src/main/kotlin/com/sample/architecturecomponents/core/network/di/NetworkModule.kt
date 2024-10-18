package com.sample.architecturecomponents.core.network.di

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.sample.architecturecomponents.core.datastore.SettingsPreference
import com.sample.architecturecomponents.core.network.BuildConfig
import com.sample.architecturecomponents.core.network.dev.DevAssetManager
import com.sample.architecturecomponents.core.network.retrofit.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesDevAssetManager(
        @ApplicationContext context: Context,
    ): DevAssetManager = DevAssetManager(context.assets::open)

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(preference: SettingsPreference): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor(preference))
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        ).build()

    @Provides
    @Singleton
    fun imageLoader(
        okHttpClient: OkHttpClient,
        @ApplicationContext application: Context,
    ): ImageLoader = ImageLoader.Builder(application)
            .callFactory { okHttpClient }
            .components { add(SvgDecoder.Factory()) }
            .respectCacheHeaders(false)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()

}
