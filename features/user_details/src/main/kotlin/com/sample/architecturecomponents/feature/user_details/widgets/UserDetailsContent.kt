package com.sample.architecturecomponents.feature.user_details.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
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
import com.sample.architecturecomponents.core.domain.ext.toFormatterDateTime
import com.sample.architecturecomponents.core.model.UserDetails
import com.sample.architecturecomponents.core.ui.ext.getEmailLink
import com.sample.architecturecomponents.core.ui.ext.getHyperLink
import com.sample.architecturecomponents.core.ui.ext.toAnnotatedString
import com.sample.architecturecomponents.core.ui.widgets.TwoSeparatedTextWidget
import com.sample.architecturecomponents.feature.user_details.R

@Composable
fun UserDetailsContent(
    userDetails: UserDetails,
    onUrlClick: (String) -> Unit,
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
            Image(
                painter = imageLoader,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .height(300.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .clickable { onUrlClick("") }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_id,
                title = { userDetails.id.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_url,
                title = { getHyperLink(userDetails.url) }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_name,
                title = { userDetails.name?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_company,
                title = { userDetails.company?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_blog,
                title = { getHyperLink(userDetails.blog) }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_location,
                title = { userDetails.location?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_location,
                title = { getEmailLink(userDetails.email, userDetails.email) }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_bio,
                title = { userDetails.bio?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_public_repos,
                title = { userDetails.bio?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_followers,
                title = { userDetails.followers?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_created,
                title = { userDetails.createdAt?.toFormatterDateTime?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_user_details_user_repos,
                title = { getHyperLink(userDetails.repositoriesUrl) }
            )
        }
    }
}