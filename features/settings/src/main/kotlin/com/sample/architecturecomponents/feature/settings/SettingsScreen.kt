package com.sample.architecturecomponents.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.architecturecomponents.core.ui.widgets.SettingsColumnBlockWidget
import com.sample.architecturecomponents.core.ui.widgets.TwoLinesButtonWidget

@Composable
fun SettingsScreen(
    showThemeDialog: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SettingsColumnBlockWidget(
            headerRes = R.string.feature_settings_ui,
        ) {
            TwoLinesButtonWidget(
                headerRes = R.string.feature_settings_theme_header,
                titleRes = R.string.feature_settings_theme_action,
                modifier = Modifier.padding(start = 32.dp, end = 16.dp),
                onClick = showThemeDialog
            )
        }

        SettingsColumnBlockWidget(
            headerRes = R.string.feature_settings_storage,
        ) {
            TwoLinesButtonWidget(
                headerRes = R.string.feature_settings_storage_capacity,
                titleRes = R.string.feature_settings_storage_clear,
                modifier = Modifier.padding(start = 32.dp, end = 16.dp),
                onClick = {

                }
            )
        }
    }


}
