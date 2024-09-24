package com.sample.architecturecomponents.core.datastore.tests

import androidx.datastore.core.CorruptionException
import com.sample.architecturecomponents.core.datastore.DataStoreProtoSerializer
import com.sample.architecturecomponents.core.datastore.ThemeBrandProto
import com.sample.architecturecomponents.core.datastore.settingsDataStore
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class DataStoreProtoSerializerTest {

    private val dataStoreProtoSerializer = DataStoreProtoSerializer()

    @Test
    fun defaultSettingsDataStore_isEmptyTest() {
        assertEquals(
            settingsDataStore { /*Default value*/ },
            dataStoreProtoSerializer.defaultValue,
        )
    }

    @Test
    fun writingAndReadingSettingsDataStore_outputsCorrectValueTest() = runTest {
        val expectedSettingsDataStore = settingsDataStore {
            useDynamicColor = false
            themeBrandValue = ThemeBrandProto.THEME_BRAND_ANDROID_VALUE
            notificationEnabled = true
        }

        val outputStream = ByteArrayOutputStream()
        expectedSettingsDataStore.writeTo(outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val actualSettingsDataStore = dataStoreProtoSerializer.readFrom(inputStream)

        assertEquals(
            expectedSettingsDataStore,
            actualSettingsDataStore,
        )
    }

    @Test(expected = CorruptionException::class)
    fun readingInvalidSettingsDataStore_throwsCorruptionExceptionTest() = runTest {
        dataStoreProtoSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
    }
}
