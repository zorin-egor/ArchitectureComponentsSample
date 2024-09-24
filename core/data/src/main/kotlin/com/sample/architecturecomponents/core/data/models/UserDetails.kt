package com.sample.architecturecomponents.core.data.models

import com.sample.architecturecomponents.core.database.model.UserDetailsEntity
import com.sample.architecturecomponents.core.model.UserDetails
import com.sample.architecturecomponents.core.network.converters.dateTimeConverter
import com.sample.architecturecomponents.core.network.models.NetworkUserDetails

internal fun UserDetails.toDetailsEntity() = UserDetailsEntity(
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
    updatedAt = updatedAt,
    reposUrl = reposUrl,
    url = url,
    hireable = hireable
)

internal fun NetworkUserDetails.toDetailsEntity() = UserDetailsEntity(
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
    createdAt = dateTimeConverter(createdAt),
    updatedAt = dateTimeConverter(updatedAt),
    reposUrl = reposUrl,
    url = htmlUrl,
    hireable = hireable ?: false
)

internal fun NetworkUserDetails.toRepositoryModel() = UserDetails(
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
    createdAt = dateTimeConverter(createdAt),
    updatedAt = dateTimeConverter(updatedAt),
    reposUrl = reposUrl,
    hireable = hireable ?: false
)

val UserDetails.repositoriesUrl: String get() = "$url?tab=repositories"