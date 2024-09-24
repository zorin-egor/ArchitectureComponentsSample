package com.sample.architecturecomponents.core.datastore.ext

import androidx.datastore.core.DataStoreFactory
import com.sample.architecturecomponents.core.datastore.DataStoreProtoSerializer
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TemporaryFolder

fun TemporaryFolder.testSettingsDataStore(
    coroutineScope: CoroutineScope,
    fileName: String,
    userPreferencesSerializer: DataStoreProtoSerializer = DataStoreProtoSerializer()
) = DataStoreFactory.create(
    serializer = userPreferencesSerializer,
    scope = coroutineScope,
) {
    newFile(fileName)
}