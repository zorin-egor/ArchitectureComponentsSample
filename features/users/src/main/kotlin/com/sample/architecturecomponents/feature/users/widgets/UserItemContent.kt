package com.sample.architecturecomponents.feature.users.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.sample.architecturecomponents.core.designsystem.icon.AppIcons
import com.sample.architecturecomponents.core.model.User
import com.sample.architecturecomponents.core.ui.widgets.ImageLoadingWidget

@Composable
fun UsersItemContent(
    user: User,
    onUserClick: (User) -> Unit,
    modifier: Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = user.avatarUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    Card(
        modifier = modifier.then(Modifier.height(100.dp)),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true),
                    onClick = { onUserClick(user) },
                )
        ) {

            ImageLoadingWidget(
                isError = isError,
                isLoading = isLoading,
                painter = imageLoader,
                placeHolder = AppIcons.UserBorder,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .weight(weight = 1.0f)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = user.login,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = TextUnit(2.0f, TextUnitType.Sp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
        }
    }
}