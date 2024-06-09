package com.sample.architecturecomponents.feature.settings.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sample.architecturecomponents.feature.settings.R

@Composable
fun SettingsThemeItem(
    onChangeThemeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.feature_settings_theme_header),
            fontSize = 14.sp,
            lineHeight = 14.sp,
            maxLines = 1,
            modifier = Modifier.wrapContentSize()
        )

        TextButton(
            onClick = onChangeThemeClick,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.feature_settings_theme_action),
                fontSize = 16.sp,
                lineHeight = 16.sp,
                maxLines = 2,
            )
        }
    }
}

//@Preview
//@Composable
//fun SettingsThemeItemPreview() {
//    SettingsThemeItem(
//        onChangeThemeClick = {}
//    )
//}
