@file:OptIn(ExperimentalMaterial3Api::class)

package com.sample.architecturecomponents.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import timber.log.Timber

@Stable
data class SearchTextDataItem(
    val id: String = "",
    val text: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedSearchTextField(
    searchQuery: String,
    options: List<SearchTextDataItem>,
    onSearchQueryChanged: (SearchTextDataItem) -> Unit,
    modifier: Modifier = Modifier,
    onSearchTriggered: ((String) -> Unit)? = null,
    contentDescriptionSearch: String? = null,
    contentDescriptionClose: String? = null,
    inputFilter: (String) -> Boolean = { "\n" !in it },
    isFocusRequest: Boolean = true,
    isExpanded: Boolean = false,
    placeholder: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Search,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Timber.d("ExposedSearchTextField($options)")

    var expanded by remember { mutableStateOf(isExpanded && !isFocusRequest && options.isNotEmpty()) }

    ExposedDropdownMenuBox(
        expanded = expanded && options.isNotEmpty(),
        onExpandedChange = {
            Timber.d("ExposedSearchTextField() - onExpandedChange($it)")
            expanded = false
        },
        modifier = modifier.padding(8.dp)
    ) {
        Timber.d("ExposedSearchTextField() - SearchTextField($searchQuery)")

        SearchTextField(
            searchQuery = searchQuery,
            contentDescriptionSearch = contentDescriptionSearch,
            contentDescriptionClose = contentDescriptionClose,
            onSearchQueryChanged = {
                Timber.d("ExposedSearchTextField() - onSearchQueryChanged($searchQuery, $it)")
                if (!expanded) {
                    expanded = true
                }
                onSearchQueryChanged(SearchTextDataItem(text = it))
            },
            onSearchTriggered = onSearchTriggered,
            inputFilter = inputFilter,
            isFocusRequest = isFocusRequest && !expanded,
            placeholder = placeholder,
            visualTransformation = visualTransformation,
            imeAction = imeAction,
            keyboardType = keyboardType,
            padding = 0.dp,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )

        DropdownMenu(
            expanded = expanded && options.isNotEmpty(),
            onDismissRequest = {
                Timber.d("ExposedSearchTextField() - onDismissRequest()")
                expanded = false
            },
            offset = DpOffset(x = 0.dp, y = 8.dp),
            modifier = Modifier.exposedDropdownSize(),
            properties = PopupProperties(
                focusable = false
            )
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.text) },
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    onClick = {
                        Timber.d("ExposedSearchTextField() - onClick($option)")
                        expanded = false
                        onSearchQueryChanged(option)
                    },
                )
            }
        }
    }
}