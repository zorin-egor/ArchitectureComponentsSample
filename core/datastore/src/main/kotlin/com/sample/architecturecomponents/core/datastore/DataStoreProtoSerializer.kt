package com.sample.architecturecomponents.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
class DataStoreProtoSerializer @Inject constructor() : Serializer<SettingsDataStore> {
    override val defaultValue: SettingsDataStore = SettingsDataStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingsDataStore =
        try {
            // readFrom is already called on the data store background thread
            SettingsDataStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: SettingsDataStore, output: OutputStream) {
        t.writeTo(output)
    }
}
