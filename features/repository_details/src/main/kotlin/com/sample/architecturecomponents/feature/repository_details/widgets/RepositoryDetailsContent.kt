package com.sample.architecturecomponents.feature.repository_details.widgets

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
import com.sample.architecturecomponents.core.designsystem.icon.Icons
import com.sample.architecturecomponents.core.domain.ext.toFormatterDateTime
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.core.ui.ext.getHyperLink
import com.sample.architecturecomponents.core.ui.ext.toAnnotatedString
import com.sample.architecturecomponents.core.ui.widgets.TwoSeparatedTextWidget
import com.sample.architecturecomponents.feature.repository_details.R

@Composable
fun RepositoryDetailsContent(
    isTopBarVisible: Boolean,
    repositoryDetails: RepositoryDetails,
    onUrlClick: (String) -> Unit,
    onShareClick: (RepositoryDetails) -> Unit,
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
                        imageVector = Icons.Share,
                        contentDescription = null,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                            .clickable { onShareClick(repositoryDetails) }
                    )
                }
            }

            repositoryDetails.name.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_repository_name,
                    title = it
                )
            }

            repositoryDetails.forks.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_forks,
                    title = it
                )
            }

            repositoryDetails.watchersCount.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_watchers,
                    title = it
                )
            }

            repositoryDetails.createdAt.toFormatterDateTime?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_created_at,
                    title = it
                )
            }

            repositoryDetails.updatedAt.toFormatterDateTime?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_updated_at,
                    title = it
                )
            }

            repositoryDetails.pushedAt.toFormatterDateTime?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_pushed_at,
                    title = it
                )
            }

            repositoryDetails.defaultBranch.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_default_branch,
                    title = it
                )
            }

            repositoryDetails.stargazersCount.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_stargazers_count,
                    title = it
                )
            }

            repositoryDetails.stargazersCount.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_stargazers_count,
                    title = it
                )
            }

            repositoryDetails.description?.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_description,
                    title = it
                )
            }

            repositoryDetails.topics.joinToString(separator = "-") { it }.toAnnotatedString()?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_topics,
                    title = it
                )
            }

            getHyperLink(repositoryDetails.license?.url)?.let {
                TwoSeparatedTextWidget(
                    headerResId = R.string.feature_repository_details_license,
                    title = it
                )
            }

        }
    }
}