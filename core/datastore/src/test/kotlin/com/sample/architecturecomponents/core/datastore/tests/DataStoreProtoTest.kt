package com.sample.architecturecomponents.core.datastore.tests

import com.sample.architecturecomponents.core.datastore.DataStoreProto
import com.sample.architecturecomponents.core.datastore.SettingsDataStoreProto
import com.sample.architecturecomponents.core.datastore.ext.testSettingsDataStore
import com.sample.architecturecomponents.core.model.DarkThemeConfig
import com.sample.architecturecomponents.core.model.ThemeBrand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DataStoreProtoTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: SettingsDataStoreProto

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = DataStoreProto(tmpFolder.testSettingsDataStore(testScope, "settings_data_store_test.pb"))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldNotificationShowIsFalseByDefaultTest() = runTest {
        assertFalse(subject.settings.first().settingsPreference.isNotificationEnabled)
    }

    @Test
    fun themeBrandByDefaultTest() = runTest {
        assertEquals(subject.settings.first().themeData.themeBrand, ThemeBrand.DEFAULT)
    }

    @Test
    fun setThemeBrandAndCheckValueTest() = runTest {
        subject.setThemeBrand(ThemeBrand.ANDROID)
        assertEquals(subject.settings.first().themeData.themeBrand, ThemeBrand.ANDROID)
    }

    @Test
    fun darkThemeConfigByDefaultTest() = runTest {
        assertEquals(subject.settings.first().themeData.darkThemeConfig, DarkThemeConfig.FOLLOW_SYSTEM)
    }

    @Test
    fun setDarkThemeConfigAndCheckValueTest() = runTest {
        subject.setDarkThemeConfig(DarkThemeConfig.DARK)
        assertEquals(subject.settings.first().themeData.darkThemeConfig, DarkThemeConfig.DARK)
    }

    @Test
    fun shouldUseDynamicColorFalseByDefaultTest() = runTest {
        assertFalse(subject.settings.first().themeData.useDynamicColor)
    }

    @Test
    fun userShouldUseDynamicColorIsTrueWhenSetTest() = runTest {
        subject.setDynamicColorPreference(true)
        assertTrue(subject.settings.first().themeData.useDynamicColor)
    }
}
