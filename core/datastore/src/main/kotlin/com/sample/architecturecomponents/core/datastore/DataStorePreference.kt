package com.sample.architecturecomponents.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sample.architecturecomponents.core.model.AppConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorePreference @Inject constructor(
    private val context: Context,
    private val appConfig: AppConfig,
    private val dataStoreName: String = SETTINGS_DATASTORE
) : SettingsPreference {

    private val Context.dataStore by preferencesDataStore(
        name = dataStoreName
    )

    private val sharedPreferences by lazy {
        context.getSharedPreferences(dataStoreName, Context.MODE_PRIVATE)
    }

    companion object {
        private const val SETTINGS_DATASTORE = "settings_datastore"
        private const val AUTH_TOKEN_KEY = "auth_token"
        private const val SINCE_USER_KEY = "since_user"
        private const val BASE_URL_KEY = "base_url"
        private val AUTH_TOKEN = stringPreferencesKey(AUTH_TOKEN_KEY)
        private val SINCE_USER = longPreferencesKey(SINCE_USER_KEY)
        private val BASE_URL = stringPreferencesKey(BASE_URL_KEY)
    }

    override suspend fun saveBaseUrl(value: String) {
        context.dataStore.edit { preferences ->
            preferences[BASE_URL] = value
        }
    }

    override suspend fun getBaseUrl(): String =
        context.dataStore.data.firstOrNull()?.get(BASE_URL) ?: appConfig.baseUrl

    override suspend fun saveAuthToken(value: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = value
        }
    }

    override suspend fun getAuthToken(): String? =
        context.dataStore.data.first()[AUTH_TOKEN]

    override suspend fun saveSinceUser(value: Long) {
        context.dataStore.edit { preferences ->
            preferences[SINCE_USER] = value
        }
    }

    override fun getSinceUser(): Flow<Long?> =
        context.dataStore.data.map { preferences ->
            preferences[SINCE_USER]
        }

    override suspend fun wipe() {
        //sharedPreferences.edit().clear().commit()
        context.dataStore.edit { it.clear() }
    }
}

