package com.sample.architecturecomponents.feature.repository_details.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.sample.architecturecomponents.core.domain.ext.toFormatterDateTime
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.core.ui.ext.getHyperLink
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_repository_name).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = getHyperLink(repositoryDetails.name),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_forks).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repositoryDetails.forks.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_watchers).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repositoryDetails.watchersCount.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_created_at).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repositoryDetails.createdAt.toFormatterDateTime,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_updated_at).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repositoryDetails.updatedAt.toFormatterDateTime,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_pushed_at).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repositoryDetails.pushedAt.toFormatterDateTime,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_default_branch).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repositoryDetails.defaultBranch,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_stargazers_count).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repositoryDetails.stargazersCount.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_description).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repositoryDetails.description ?: "-",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_topics).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repositoryDetails.topics.joinToString(separator = "-") { it },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_repository_details_license).plus(":"),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = getHyperLink(repositoryDetails.license?.url ?: "-"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}