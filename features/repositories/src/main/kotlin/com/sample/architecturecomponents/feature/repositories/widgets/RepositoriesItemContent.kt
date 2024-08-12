package com.sample.architecturecomponents.feature.repositories.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import coil.compose.rememberAsyncImagePainter
import com.sample.architecturecomponents.core.designsystem.icon.Icons
import com.sample.architecturecomponents.core.domain.ext.toFormatterDateTime
import com.sample.architecturecomponents.core.model.Repository
import com.sample.architecturecomponents.core.ui.ext.BorderCircleImageRequest
import com.sample.architecturecomponents.core.ui.widgets.ImageLoadingWidget
import kotlinx.datetime.Clock

@Composable
fun RepositoriesItemContent(
    repository: Repository,
    onRepositoryClick: (Repository) -> Unit,
    modifier: Modifier
) {
    val materialTheme = MaterialTheme.colorScheme
    val isLoading = remember { mutableStateOf(true) }
    val isError = remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        BorderCircleImageRequest(
            url = repository.avatarUrl,
            isError = isError,
            isLoading = isLoading,
            strokeColor = materialTheme.onSurfaceVariant,
            strokeWidth = 1.dp
        ).build()
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true),
                    onClick = { onRepositoryClick(repository) },
                )
        ) {
            val (image, name, desc, starsIcon, forksIcon, startTitle, forksTitle, dateTime) = createRefs()
            val verticalChainText = createVerticalChain(name, desc, dateTime, chainStyle = ChainStyle.Packed(0.5f))
            val verticalChainIcons = createVerticalChain(starsIcon, startTitle, forksIcon, forksTitle, chainStyle = ChainStyle.Packed(0.5f))

            constrain(verticalChainText) {}
            constrain(verticalChainIcons) {}

            ImageLoadingWidget(
                painter = imageLoader,
                contentScale = ContentScale.Crop,
                isError = isError.value,
                isLoading = isLoading.value,
                placeHolder = Icons.UserBorder,
                placeholderColorFilter = ColorFilter.tint(color = materialTheme.onSurfaceVariant),
                modifier = Modifier
                    .constrainAs(image) {
                        height = Dimension.wrapContent
                        width = Dimension.wrapContent
                        visibility = if (repository.avatarUrl != null)
                            Visibility.Visible else Visibility.Gone

                        start.linkTo(anchor = parent.start, margin = 16.dp)
                        top.linkTo(anchor = parent.top, margin = 16.dp)
                        bottom.linkTo(anchor = parent.bottom, margin = 16.dp)
                    }
                    .size(48.dp)
            )

            Text(
                text = repository.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp,
                modifier = Modifier
                    .constrainAs(name) {
                        height = Dimension.wrapContent
                        width = Dimension.fillToConstraints

                        linkTo(
                            top = parent.top,
                            bottom = desc.top,
                            start = image.end,
                            end = starsIcon.start,
                            startMargin = 16.dp,
                            endMargin = 16.dp,
                            topMargin = 16.dp,
                            bottomGoneMargin = 16.dp,
                            horizontalBias = 0.0f,
                            verticalBias = 0.5f
                        )
                    }
            )

            Text(
                text = repository.description ?: "",
                fontSize = 14.sp,
                lineHeight = 14.sp,
                maxLines = 3,
                fontWeight = FontWeight.Light,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .constrainAs(desc) {
                        height = Dimension.wrapContent
                        width = Dimension.fillToConstraints
                        visibility = if (repository.description?.isNotEmpty() == true)
                            Visibility.Visible else Visibility.Gone

                        linkTo(
                            top = name.bottom,
                            bottom = dateTime.top,
                            start = name.start,
                            end = starsIcon.start,
                            endMargin = 16.dp,
                            topMargin = 4.dp,
                            bottomMargin = 16.dp,
                            horizontalBias = 0.0f
                        )
                    }
                    .padding(top = 4.dp),
            )

            Text(
                text = repository.updatedAt.toFormatterDateTime ?: "-",
                fontSize = 10.sp,
                lineHeight = 10.sp,
                maxLines = 3,
                fontWeight = FontWeight.Light,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .constrainAs(dateTime) {
                        height = Dimension.wrapContent
                        width = Dimension.fillToConstraints
                        linkTo(
                            top = desc.bottom,
                            bottom = parent.bottom,
                            start = name.start,
                            end = starsIcon.start,
                            endMargin = 16.dp,
                            topMargin = 4.dp,
                            bottomMargin = 16.dp,
                            horizontalBias = 0.0f
                        )
                    }
                    .padding(top = 4.dp),
            )

            Icon(
                imageVector = Icons.Start,
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(starsIcon) {
                        height = Dimension.value(24.dp)
                        width = Dimension.value(24.dp)
                        top.linkTo(anchor = parent.top, margin = 16.dp)
                        end.linkTo(anchor = parent.end, margin = 16.dp)
                        bottom.linkTo(anchor = startTitle.top)
                    }
                    .alpha(0.8f),
            )

            Text(
                text = repository.stargazersCount.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraLight,
                maxLines = 1,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                modifier = Modifier
                    .constrainAs(startTitle) {
                        height = Dimension.wrapContent
                        width = Dimension.wrapContent
                        top.linkTo(anchor = starsIcon.bottom, margin = 2.dp)
                        start.linkTo(anchor = starsIcon.start)
                        end.linkTo(anchor = starsIcon.end)
                        bottom.linkTo(anchor = forksIcon.top)
                    },
            )

            Icon(
                imageVector = Icons.ForkLeft,
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(forksIcon) {
                        height = Dimension.value(24.dp)
                        width = Dimension.value(24.dp)
                        top.linkTo(anchor = startTitle.bottom, margin = 4.dp)
                        start.linkTo(anchor = starsIcon.start)
                        end.linkTo(anchor = starsIcon.end)
                        bottom.linkTo(anchor = forksTitle.top)
                    }
                    .padding(top = 4.dp)
                    .alpha(0.8f),
            )

            Text(
                text = repository.forks.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraLight,
                maxLines = 1,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                modifier = Modifier
                    .constrainAs(forksTitle) {
                        height = Dimension.wrapContent
                        width = Dimension.wrapContent
                        top.linkTo(anchor = forksIcon.bottom, margin = 2.dp)
                        start.linkTo(anchor = starsIcon.start)
                        end.linkTo(anchor = starsIcon.end)
                        bottom.linkTo(anchor = parent.bottom, margin = 16.dp)
                    },
            )
        }
    }
}

@Preview
@Composable
fun RepositoriesItemContentPreview() {
    RepositoriesItemContent(
        repository = Repository(
            id = 0,
            userId = 0,
            owner = "owner",
            avatarUrl = "",
            name = "name",
            forks = 0,
            watchersCount = 0,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            stargazersCount = 0,
            description = "description",
        ),
        onRepositoryClick = {},
        modifier = Modifier.height(200.dp)
    )
}