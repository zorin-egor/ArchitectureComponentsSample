package com.sample.architecturecomponents.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sample.architecturecomponents.core.designsystem.icon.AppIcons
import timber.log.Timber

@Composable
fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearchTriggered: ((String) -> Unit)? = null,
    contentDescriptionSearch: String? = null,
    contentDescriptionClose: String? = null,
    inputFilter: (String) -> Boolean = { "\n" !in it },
    isFocusRequest: Boolean = true,
    placeholder: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Search,
    keyboardType: KeyboardType = KeyboardType.Text,
    padding: Dp = 8.dp
) {
    SearchTextField(
        searchQuery = TextFieldValue(
            text = searchQuery,
            selection = TextRange(searchQuery.length)
        ),
        onSearchQueryChanged = onSearchQueryChanged,
        modifier = modifier,
        onSearchTriggered = onSearchTriggered,
        contentDescriptionSearch = contentDescriptionSearch,
        contentDescriptionClose = contentDescriptionClose,
        inputFilter = inputFilter,
        isFocusRequest = isFocusRequest,
        placeholder = placeholder,
        visualTransformation = visualTransformation,
        imeAction = imeAction,
        keyboardType = keyboardType,
        padding = padding,
    )
}

@Composable
fun SearchTextField(
    searchQuery: TextFieldValue,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearchTriggered: ((String) -> Unit)? = null,
    contentDescriptionSearch: String? = null,
    contentDescriptionClose: String? = null,
    inputFilter: (String) -> Boolean = { "\n" !in it },
    isFocusRequest: Boolean = true,
    placeholder: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Search,
    keyboardType: KeyboardType = KeyboardType.Text,
    padding: Dp = 8.dp
) {
    Timber.d("SearchTextField($searchQuery)")

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchAction = remember {{
        Timber.d("SearchTextField() - onSearchAction()")
        keyboardController?.hide()
        onSearchTriggered?.invoke(searchQuery.text)
    }}

    val onKeyAction: (KeyEvent) -> Boolean = remember {{
        Timber.d("SearchTextField() - onKeyEvent($it)")
        if (it.key == Key.Enter) {
            onSearchAction()
            true
        } else {
            false
        }
    }}

    val onValueChangeAction: (TextFieldValue) -> Unit = remember {{
        if (inputFilter(it.text) && it.text != searchQuery.text) {
            Timber.d("SearchTextField() - onValueChangeAction($it)")
            onSearchQueryChanged(it.text)
        }
    }}

    val onValueChangeClearAction: () -> Unit = remember {{
        Timber.d("SearchTextField() - onValueChangeClearAction()")
        onSearchQueryChanged("")
    }}

    val onSearchDerived: KeyboardActionScope.() -> Unit = remember {{ onSearchAction() }}

    TextField(
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = AppIcons.Search,
                contentDescription = contentDescriptionSearch,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            if (searchQuery.text.isNotEmpty()) {
                IconButton(
                    onClick = onValueChangeClearAction,
                ) {
                    Icon(
                        imageVector = AppIcons.Close,
                        contentDescription = contentDescriptionClose,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = onValueChangeAction,
        placeholder = { if (placeholder != null) Text(text = stringResource(id = placeholder)) },
        modifier = modifier
            .fillMaxWidth()
            .padding(padding)
            .onKeyEvent(onKeyAction)
            .testTag("searchTextField")
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(16.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = keyboardType,
            capitalization = KeyboardCapitalization.None
        ),
        keyboardActions = KeyboardActions(
            onSearch = onSearchDerived,
        ),
        singleLine = true,
        visualTransformation = visualTransformation
    )

    if (isFocusRequest) {
        LaunchedEffect(focusRequester) {
            Timber.d("SearchTextField() - LaunchedEffect($this)")
            focusRequester.requestFocus()
        }
    }
}

@Preview("Search text field")
@Composable
fun SearchTextFieldPreview() {
    val someText = "Some text"
    SearchTextField(
        searchQuery = TextFieldValue(
            text = someText,
            selection = TextRange(someText.length)
        ),
        contentDescriptionSearch = "contentDescriptionSearch",
        contentDescriptionClose = "contentDescriptionClose",
        onSearchQueryChanged = {},
        onSearchTriggered = {}
    )
}