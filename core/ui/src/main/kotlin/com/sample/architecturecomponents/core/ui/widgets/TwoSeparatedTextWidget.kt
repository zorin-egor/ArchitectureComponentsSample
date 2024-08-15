package com.sample.architecturecomponents.core.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TwoSeparatedTextWidget(
    @StringRes headerResId: Int,
    title: AnnotatedString,
    modifier: Modifier = Modifier,
    separator: String = ":",
    spacerHeight: Dp = 8.dp
) {
    Spacer(modifier = Modifier.height(height = spacerHeight))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(id = headerResId).plus(separator),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}