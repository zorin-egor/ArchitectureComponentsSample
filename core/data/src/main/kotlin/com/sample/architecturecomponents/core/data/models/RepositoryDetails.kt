package com.sample.architecturecomponents.core.data.models

import com.sample.architecturecomponents.core.database.model.RepositoryDetailsEntity
import com.sample.architecturecomponents.core.model.RepositoryDetails
import com.sample.architecturecomponents.core.network.converters.dateTimeConverter
import com.sample.architecturecomponents.core.network.models.common.NetworkRepository

internal fun RepositoryDetails.toRepositoryDetailsEntity() = RepositoryDetailsEntity(
    repoId = id,
    userId = userId,
    owner = userLogin,
    avatarUrl = avatarUrl,
    name = name,
    htmlUrl = htmlUrl,
    nodeId = nodeId,
    forks = forks,
    watchersCount = watchersCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
    pushedAt = pushedAt,
    defaultBranch = defaultBranch,
    stargazersCount = stargazersCount,
    description = description,
    tagsUrl = tagsUrl,
    branchesUrl = branchesUrl,
    commitsUrl = commitsUrl,
    topics = topics,
    licence = license
)

internal fun NetworkRepository.toRepositoryDetailsEntity() = RepositoryDetailsEntity(
    repoId = id,
    userId = owner.id,
    owner = owner.login,
    avatarUrl = owner.avatarUrl,
    name = name,
    htmlUrl = htmlUrl,
    nodeId = nodeId,
    forks = forks,
    watchersCount = watchersCount,
    createdAt = dateTimeConverter(createdAt),
    updatedAt = dateTimeConverter(updatedAt),
    pushedAt = pushedAt?.let(::dateTimeConverter),
    defaultBranch = defaultBranch,
    stargazersCount = stargazersCount,
    description = description,
    tagsUrl = tagsUrl,
    branchesUrl = branchesUrl,
    commitsUrl = commitsUrl,
    topics = topics,
    licence = networkLicense?.toLicenseEntity()
)

internal fun NetworkRepository.toRepositoryDetailsModel() = RepositoryDetails(
    id = id,
    userId = owner.id,
    userLogin = owner.login,
    avatarUrl = owner.avatarUrl,
    name = name,
    htmlUrl = htmlUrl,
    nodeId = nodeId,
    forks = forks,
    watchersCount = watchersCount,
    createdAt = dateTimeConverter(createdAt),
    updatedAt = dateTimeConverter(updatedAt),
    pushedAt = pushedAt?.let(::dateTimeConverter),
    defaultBranch = defaultBranch,
    stargazersCount = stargazersCount,
    description = description,
    tagsUrl = tagsUrl,
    branchesUrl = branchesUrl,
    commitsUrl = commitsUrl,
    topics = topics,
    license = networkLicense?.toLicenseEntity()
)

internal fun List<NetworkRepository>.toRepositoryDetailsEntities(): List<RepositoryDetailsEntity> = map { it.toRepositoryDetailsEntity() }

internal fun List<NetworkRepository>.toRepositoryDetailsModels(): List<RepositoryDetails> = map { it.toRepositoryDetailsModel() }
