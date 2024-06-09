package com.sample.architecturecomponents.core.data.repositories.settings

import com.sample.architecturecomponents.core.datastore.SettingsDataStoreProto
import javax.inject.Inject

internal class SettingsRepositoryImpl @Inject constructor(
    private val dataSourceProto: SettingsDataStoreProto,
) : SettingsRepository {


}