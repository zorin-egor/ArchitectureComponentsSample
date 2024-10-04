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
        assertEquals(BASE_URL, subject.getBaseUrl())
    }

    @Test
    fun checkBaseUrlSetAndGetTest() = runTest {
        val testUrl = "some_test_url"
        subject.saveBaseUrl(testUrl)
        assertEquals(testUrl, subject.getBaseUrl())
    }

    @Test
    fun checkAuthTokenByDefaultTest() = runTest {
        assertEquals(null, subject.getAuthToken())
    }

    @Test
    fun checkAuthTokenSetAndGetTest() = runTest {
        val testToken = "some_test_token"
        subject.saveAuthToken(testToken)
        assertEquals(testToken, subject.getAuthToken())
    }

    @Test
    fun checkSinceUserByDefaultTest() = runTest {
        assertEquals(null, subject.getSinceUser().first())
    }

    @Test
    fun checkSinceUserSetAndGetTest() = runTest {
        val testSinceUser = 1L
        subject.saveSinceUser(testSinceUser)
        assertEquals(testSinceUser, subject.getSinceUser().first())
    }

}