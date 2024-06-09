package com.sample.architecturecomponents.core.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsColumnBlockWidget(
    @StringRes headerRes: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 16.dp),
    ) {
        HorizontalDivider()
        Text(
            text = stringResource(id = headerRes),
            fontSize = 12.sp,
            lineHeight = 12.sp,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp, start = 32.dp, end = 16.dp)
        )
        content()
    }
}