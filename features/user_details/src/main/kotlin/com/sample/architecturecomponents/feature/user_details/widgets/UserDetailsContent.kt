package com.sample.architecturecomponents.feature.user_details.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.sample.architecturecomponents.core.data.models.repositoriesUrl
import com.sample.architecturecomponents.core.designsystem.icon.AppIcons
import com.sample.architecturecomponents.core.domain.ext.toFormatterDateTime
import com.sample.architecturecomponents.core.model.UserDetails
import com.sample.architecturecomponents.core.ui.ext.getEmailLink
import com.sample.architecturecomponents.core.ui.ext.getHyperLink
import com.sample.architecturecomponents.core.ui.ext.toAnnotatedString
import com.sample.architecturecomponents.core.ui.widgets.TwoSeparatedTextWidget
import com.sample.architecturecomponents.feature.user_details.R

@Composable
fun UserDetailsContent(
    isTopBarVisible: Boolean,
    userDetails: UserDetails,
    onShareClick: (UserDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = userDetails.avatarUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    val onShareProfileClick = remember {{ onShareClick(userDetails) }}
    val scroll = rememberScrollState()

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(scroll)
        ) {
            Box(modifier = Modifier.wrapContentSize()) {
                Image(
                    painter = imageLoader,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .height(300.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                )
                if (!isTopBarVisible) {
                    Icon(
                        imageVector = AppIcons.Share,
                        contentDescription = null,
                        modifier = Modifier.wrapContentSize()
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                            .clickable(onClick = onShareProfileClick)
                    )
                }
            }

            userDetails.id.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_id,
                    title = it
                )
            }

            getHyperLink(userDetails.url)?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_url,
                    title = it
                )
            }

            userDetails.name?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_name,
                    title = it
                )
            }

            userDetails.company?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_company,
                    title = it
                )
            }

            getHyperLink(userDetails.blog)?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_blog,
                    title = it
                )
            }

            userDetails.location?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_location,
                    title = it
                )
            }

            getEmailLink(userDetails.email, userDetails.email)?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_location,
                    title = it
                )
            }

            userDetails.bio?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_bio,
                    title = it
                )
            }

            userDetails.bio?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_public_repos,
                    title = it
                )
            }

            userDetails.followers?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_followers,
                    title = it
                )
            }

            userDetails.createdAt?.toFormatterDateTime?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_created,
                    title = it
                )
            }

            getHyperLink(userDetails.repositoriesUrl)?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_user_details_user_repos,
                    title = it
                )
            }
        }
    }
}