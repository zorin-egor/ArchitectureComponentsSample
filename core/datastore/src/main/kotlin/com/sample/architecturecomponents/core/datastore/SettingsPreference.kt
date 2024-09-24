package com.sample.architecturecomponents.core.datastore

import kotlinx.coroutines.flow.Flow

interface SettingsPreference {

    suspend fun saveBaseUrl(value: String)

    suspend fun getBaseUrl(): String

    suspend fun saveAuthToken(value: String)

    suspend fun getAuthToken(): String?

    suspend fun saveSinceUser(value: Long)

    fun getSinceUser(): Flow<Long?>

    suspend fun wipe()
}