package com.sample.architecturecomponents.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataStorePreference @Inject constructor(
    private val context: Context
) : SettingsPreference {

    private val Context.dataStore by preferencesDataStore(
        name = SETTINGS_DATASTORE
    )

    private val sharedPreferences by lazy {
        context.getSharedPreferences(SETTINGS_DATASTORE, Context.MODE_PRIVATE)
    }

    companion object {
        private const val SETTINGS_DATASTORE = "settings_datastore"
        private const val AUTH_TOKEN_KEY = "auth_token"
        private const val SINCE_USER_KEY = "since_user"
        private val AUTH_TOKEN = stringPreferencesKey(AUTH_TOKEN_KEY)
        private val SINCE_USER = longPreferencesKey(SINCE_USER_KEY)
    }

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
}

