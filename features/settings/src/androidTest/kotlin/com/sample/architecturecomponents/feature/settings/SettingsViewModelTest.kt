package com.sample.architecturecomponents.feature.settings

import android.app.Activity
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.sample.architecturecomponents.core.model.SettingsData
import com.sample.architecturecomponents.core.model.SettingsPreference
import com.sample.architecturecomponents.core.testing.tests.notifications.NotifierTest
import com.sample.architecturecomponents.core.testing.tests.repositories.SettingsRepositoryTestImpl
import com.sample.architecturecomponents.core.testing.tests.util.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val repository = SettingsRepositoryTestImpl()

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        viewModel = SettingsViewModel(
            context = context,
            settingsRepository = repository,
            notifier = NotifierTest(),
            mainScreenClass = Activity::class.java,
        )
    }

    @Test
    fun loadingSettingsViewModelTest() {
        assertEquals(SettingsUiState.Loading, viewModel.state.value)
    }

    @Test
    fun setNotificationSettingsTest() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.state.collect() }

        repository.setNotificationEnable(false)

        assertEquals(
            SettingsUiState.Success(
                SettingsData(
                    cacheSize = "10MB",
                    version = "0.0.1",
                    preference = SettingsPreference(
                        isNotificationEnabled = false
                    ),
                ),
            ),
            viewModel.state.value,
        )
    }

    @Test
    fun clearCacheSettingsTest() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.state.collect() }

        repository.clearCache()

        assertEquals(
            SettingsUiState.Success(
                SettingsData(
                    cacheSize = "",
                    version = "0.0.1",
                    preference = SettingsPreference(
                        isNotificationEnabled = true
                    ),
                ),
            ),
            viewModel.state.value,
        )
    }

}