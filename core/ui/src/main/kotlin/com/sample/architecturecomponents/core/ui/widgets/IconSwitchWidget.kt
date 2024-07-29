package com.sample.architecturecomponents.core.ui.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconSwitchWidget(
    @StringRes headerRes: Int,
    isSwitchChecked: Boolean,
    onSwitch: (isCheck: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes icon: Int? = null,
) {
    var isChecked by remember { mutableStateOf(isSwitchChecked) }
    val modifierDefault = Modifier
        .clip(RectangleShape)
        .clickable(
            enabled = enabled,
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = true),
            onClick = {
                isChecked = !isChecked
                onSwitch(isChecked)
            }
        )
        .wrapContentHeight()
        .fillMaxWidth()

    Row(
        modifier = modifierDefault
            .then(modifier)
            .padding(horizontal = 0.dp, vertical = 8.dp),
    ) {
        if (icon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = stringResource(id = headerRes),
            fontSize = 16.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 0.dp).align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.fillMaxWidth().weight(1.0f))

        Switch(
            enabled = enabled,
            checked = isChecked,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .scale(0.7f),
            onCheckedChange = {
                isChecked = !isChecked
                onSwitch(isChecked)
            }
        )
    }
}