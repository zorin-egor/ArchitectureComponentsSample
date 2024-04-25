package com.sample.architecturecomponents.core.data.models

import com.sample.architecturecomponents.core.database.model.DetailsEntity
import com.sample.architecturecomponents.core.model.Details
import com.sample.architecturecomponents.core.network.models.NetworkDetails

internal fun Details.toDetailsEntity() = DetailsEntity(
    id = id,
    avatarUrl = avatarUrl,
    userId = id,
    name = name,
    company = company,
    blog = blog,
    location = location,
    email = email,
    bio = bio,
    publicRepos = publicRepos,
    publicGists = publicGists,
    followers = followers,
    following = following,
    createdAt = createdAt,
    reposUrl = reposUrl,
    url = url
)

internal fun NetworkDetails.toDetailsEntity() = DetailsEntity(
    userId = id,
    avatarUrl = avatarUrl,
    name = name,
    company = company,
    blog = blog,
    location = location,
    email = email,
    bio = bio,
    publicRepos = publicRepos,
    publicGists = publicGists,
    followers = followers,
    following = following,
    createdAt = createdAt,
    reposUrl = reposUrl,
    url = htmlUrl
)

internal fun NetworkDetails.toExternalModel() = Details(
    id = id,
    url = htmlUrl,
    avatarUrl = avatarUrl,
    name = name,
    company = company,
    blog = blog,
    location = location,
    email = email,
    bio = bio,
    publicRepos = publicRepos,
    publicGists = publicGists,
    followers = followers,
    following = following,
    createdAt = createdAt,
    reposUrl = reposUrl
)

val Details.repositoriesUrl: String get() = "$url?tab=repositories"