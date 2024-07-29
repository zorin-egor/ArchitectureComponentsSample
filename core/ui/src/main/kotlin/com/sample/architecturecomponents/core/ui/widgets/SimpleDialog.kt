package com.sample.architecturecomponents.core.ui.widgets

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.DialogProperties
import com.sample.architecturecomponents.core.designsystem.component.HtmlText

@Composable
fun SimpleDialog(
    dialogTitle: String,
    dialogText: String,
    confirmText: String,
    dismissText: String,
    onConfirmation: () -> Unit,
    icon: ImageVector? = null,
    dismissOnClickOutside: Boolean = true,
    onCancel: () -> Unit = {},
) {
    AlertDialog(
        icon = {
            icon?.let {
                Icon(imageVector = icon, contentDescription = "Dialog icon")
            }
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            HtmlText(html = dialogText)
        },
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = dismissText)
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = dismissOnClickOutside
        )
    )
}