package com.sample.architecturecomponents.feature.users.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.architecturecomponents.core.model.User
import com.sample.architecturecomponents.core.ui.widgets.setEdgeEvents
import com.sample.architecturecomponents.feature.users.UsersUiState

@Composable
fun ItemsUsersContent(
    state: UsersUiState.Success,
    onUserClick: (User) -> Unit,
    onBottomEvent: () -> Unit,
    modifier: Modifier = Modifier
) {
    println("ItemsUsersContent()")

    val listState = rememberLazyListState()

    listState.setEdgeEvents(
        debounce = 1000,
        prefetch = 3,
        onTopList = { index ->
            println("ItemsUsersContent() - onTopList: $index")
        },
        onBottomList = { index ->
            println("ItemsUsersContent() - onBottomList: $index")
            onBottomEvent()
        }
    )

    Column(modifier = modifier.then(Modifier.fillMaxWidth().fillMaxHeight())) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth().weight(weight = 1.0f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.users,
                key = { it.id }
            ) { user ->
                UsersItemContent(user, onUserClick, Modifier.fillMaxWidth().height(250.dp))
            }
        }

        if (state.isBottomProgress) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(4.dp))
        }
    }
}