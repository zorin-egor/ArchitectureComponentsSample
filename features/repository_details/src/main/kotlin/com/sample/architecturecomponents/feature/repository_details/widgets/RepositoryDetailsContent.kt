package com.sample.architecturecomponents.feature.repository_details.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.sample.architecturecomponents.core.domain.ext.toFormatterDateTime
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.core.ui.ext.getHyperLink
import com.sample.architecturecomponents.core.ui.ext.toAnnotatedString
import com.sample.architecturecomponents.core.ui.widgets.TwoSeparatedTextWidget
import com.sample.architecturecomponents.feature.repository_details.R

@Composable
fun RepositoryDetailsContent(
    repositoryDetails: RepositoryDetails,
    onUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = repositoryDetails.avatarUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    val context = LocalContext.current
    val scroll = rememberScrollState()
    val scheme = MaterialTheme.colorScheme

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
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_repository_name,
                title = { repositoryDetails.name.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_forks,
                title = { repositoryDetails.forks.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_watchers,
                title = { repositoryDetails.watchersCount.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_created_at,
                title = { repositoryDetails.createdAt.toFormatterDateTime?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_updated_at,
                title = { repositoryDetails.updatedAt.toFormatterDateTime?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_pushed_at,
                title = { repositoryDetails.pushedAt.toFormatterDateTime?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_default_branch,
                title = { repositoryDetails.defaultBranch.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_stargazers_count,
                title = { repositoryDetails.stargazersCount.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_stargazers_count,
                title = { repositoryDetails.stargazersCount.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_description,
                title = { repositoryDetails.description?.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_topics,
                title = { repositoryDetails.topics.joinToString(separator = "-") { it }.toAnnotatedString() }
            )

            TwoSeparatedTextWidget(
                headerResId = R.string.feature_repository_details_license,
                title = { getHyperLink(repositoryDetails.license?.url) }
            )
        }
    }
}