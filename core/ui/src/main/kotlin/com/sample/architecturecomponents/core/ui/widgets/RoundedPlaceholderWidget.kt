package com.sample.architecturecomponents.core.ui.widgets

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sample.architecturecomponents.core.designsystem.icon.AppIcons
import com.sample.architecturecomponents.core.designsystem.theme.AppTheme
import com.sample.architecturecomponents.core.ui.R

@Composable
fun RoundedPlaceholderWidget(
    header: Int,
    image: ImageVector,
    modifier: Modifier = Modifier,
    title: Int? = null,
    cornerRadius: Dp = 16.dp,
    imageContentDescription: Int? = null
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(cornerRadius, cornerRadius, cornerRadius, cornerRadius),
    ) {
        SimplePlaceholderContent(
            header = header,
            title = title,
            image = image,
            imageContentDescription = imageContentDescription
        )
    }
}

@Preview(widthDp = 200, heightDp = 300)
@Composable
fun RoundedPlaceholderWidgetPreview() {
    AppTheme {
        RoundedPlaceholderWidget(
            header = R.string.empty_placeholder_header,
            title = R.string.empty_placeholder_title,
            image = AppIcons.Empty,
            imageContentDescription = R.string.empty_placeholder_header
        )
    }
}
