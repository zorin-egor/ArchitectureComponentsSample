package com.sample.architecturecomponents.core.datastore.tests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.sample.architecturecomponents.core.datastore.DataStorePreference
import com.sample.architecturecomponents.core.datastore.SettingsPreference
import com.sample.architecturecomponents.core.model.AppConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals


class DataStorePreferenceTest {

    companion object {
        private const val BASE_URL = "base_url"
        private const val STORE_FILE_NAME = "store_file_test"
    }

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val storeFileName = "${STORE_FILE_NAME}_${System.currentTimeMillis()}"

    lateinit var subject: SettingsPreference

    @Before
    fun setup() {
        subject = DataStorePreference(
            context = context,
            appConfig = AppConfig(
                appVersion = "0.0.0",
                appCode = 0,
                baseUrl = BASE_URL
            ),
            dataStoreName = storeFileName
        )

        runTest { subject.wipe() }
    }

    @After
    fun tearDown() = runTest {
        File(context.filesDir, "datastore").listFiles()
            ?.find { it.isFile && it.name.startsWith(storeFileName) }
            ?.delete()
    }

    @Test
    fun checkBaseUrlByDefaultTest() = runTest {
        assertEquals(subject.getBaseUrl(), BASE_URL)
    }

    @Test
    fun checkBaseUrlSetAndGetTest() = runTest {
        val testUrl = "some_test_url"
        subject.saveBaseUrl(testUrl)
        assertEquals(subject.getBaseUrl(), testUrl)
    }

    @Test
    fun checkAuthTokenByDefaultTest() = runTest {
        assertEquals(subject.getAuthToken(), null)
    }

    @Test
    fun checkAuthTokenSetAndGetTest() = runTest {
        val testToken = "some_test_token"
        subject.saveAuthToken(testToken)
        assertEquals(subject.getAuthToken(), testToken)
    }

    @Test
    fun checkSinceUserByDefaultTest() = runTest {
        assertEquals(subject.getSinceUser().first(), null)
    }

    @Test
    fun checkSinceUserSetAndGetTest() = runTest {
        val testSinceUser = 1L
        subject.saveSinceUser(testSinceUser)
        assertEquals(subject.getSinceUser().first(), testSinceUser)
    }

}