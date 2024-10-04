package com.sample.architecturecomponents.core.data.repositories.settings

import android.content.Context
import com.sample.architecturecomponents.core.common.tools.clearCache
import com.sample.architecturecomponents.core.common.tools.getCacheSize
import com.sample.architecturecomponents.core.datastore.SettingsDataStoreProto
import com.sample.architecturecomponents.core.di.Dispatcher
import com.sample.architecturecomponents.core.di.Dispatchers
import com.sample.architecturecomponents.core.di.IoScope
import com.sample.architecturecomponents.core.model.AppConfig
import com.sample.architecturecomponents.core.model.SettingsData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class SettingsRepositoryImpl @Inject constructor(
    private val dataSourceProto: SettingsDataStoreProto,
    @ApplicationContext private val context: Context,
    @IoScope private val ioScope: CoroutineScope,
    @Dispatcher(Dispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val config: AppConfig
) : SettingsRepository {

    private val refresh = MutableStateFlow(value = true)

    private suspend fun refreshSettings() {
        refresh.emit(refresh.first().not())
    }

    override val settingsData: Flow<SettingsData> = dataSourceProto.settings
        .combine(refresh) { settingsData, refresh ->
            SettingsData(
                cacheSize = context.getCacheSize(),
                version = config.appVersion,
                preference = settingsData.settingsPreference
            )
        }
        .flowOn(dispatcher)

    override fun setNotificationEnable(value: Boolean) {
        ioScope.launch {
            dataSourceProto.setNotificationEnabled(value)
        }
    }

    override fun clearCache() {
        ioScope.launch {
            context.clearCache()
            refreshSettings()
        }
    }

}