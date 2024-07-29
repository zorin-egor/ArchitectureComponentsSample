package com.sample.architecturecomponents.feature.settings

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.architecturecomponents.core.model.SettingsData
import com.sample.architecturecomponents.core.ui.ext.openBrowser
import com.sample.architecturecomponents.core.ui.widgets.IconSwitchWidget
import com.sample.architecturecomponents.core.ui.widgets.SettingsColumnBlockWidget
import com.sample.architecturecomponents.core.ui.widgets.SimpleDialog
import com.sample.architecturecomponents.core.ui.widgets.TwoLinesButtonWidget
import timber.log.Timber
import com.sample.architecturecomponents.core.ui.R as UiR

@Composable
fun SettingsScreen(
    showThemeDialog: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {

    val settingsUiState: SettingsUiState by viewModel.state.collectAsStateWithLifecycle()
    val settingsAction: SettingsActions? by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    val context = LocalContext.current
    val mainUrl = stringResource(id = R.string.feature_settings_main_url)
    var isCapacityDialog by remember { mutableStateOf(false) }

    val settingsUiValue = settingsUiState
    var settingsData: SettingsData? = null
    if (settingsUiValue is SettingsUiState.Success) {
        settingsData = settingsUiValue.settings
    }

    Timber.d("SettingsScreen() - settings ui state: $settingsUiValue")

    if (isCapacityDialog) {
        SimpleDialog(
            dialogTitle = stringResource(id = R.string.feature_settings_storage_clear),
            dialogText = stringResource(id = R.string.feature_settings_storage_clear_question, settingsData?.cacheSize ?: ""),
            confirmText = stringResource(id = UiR.string.ok),
            dismissText = stringResource(id = UiR.string.cancel),
            onConfirmation = {
                viewModel.clearCache()
                isCapacityDialog = false
            },
            onCancel = {
                isCapacityDialog = false
            })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
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
                header = stringResource(id = R.string.feature_settings_storage_capacity),
                title = "${stringResource(id = R.string.feature_settings_storage_clear)} ${settingsData?.cacheSize ?: ""}",
                modifier = Modifier.padding(start = 32.dp, end = 16.dp),
                onClick = {
                    isCapacityDialog = true
                }
            )
        }

        SettingsColumnBlockWidget(
            headerRes = R.string.feature_settings_notification,
        ) {
            IconSwitchWidget(
                headerRes = R.string.feature_settings_notification_switch_on_off,
                modifier = Modifier.padding(start = 32.dp, end = 16.dp),
                isSwitchChecked = settingsData?.preference?.isNotificationEnabled ?: true,
                enabled = settingsData?.preference != null,
                onSwitch = {
                    viewModel.setNotificationEnabled(it)
                }
            )
        }

        Spacer(modifier = Modifier
            .fillMaxHeight()
            .weight(1.0f))

        Text(
            text = stringResource(id = UiR.string.repo),
            fontSize = 14.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(horizontal = 0.dp)
                .align(Alignment.CenterHorizontally)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    context.openBrowser(
                        uri = Uri.parse(mainUrl),
                        toolbarColor = Color.Blue.toArgb()
                    )
                }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(id = UiR.string.version, settingsData?.version ?: "-"),
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(horizontal = 0.dp)
                .align(Alignment.CenterHorizontally)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
    }

}
