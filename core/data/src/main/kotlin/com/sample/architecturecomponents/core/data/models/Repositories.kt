package com.sample.architecturecomponents.core.data.models

import com.sample.architecturecomponents.core.database.model.RepositoryEntity
import com.sample.architecturecomponents.core.model.License
import com.sample.architecturecomponents.core.model.Repository
import com.sample.architecturecomponents.core.network.converters.dateTimeConverter
import com.sample.architecturecomponents.core.network.models.common.NetworkItem
import com.sample.architecturecomponents.core.network.models.common.NetworkLicense

internal fun Repository.toRepoEntity() = RepositoryEntity(
    repoId = id,
    userId = userId,
    avatarUrl = avatarUrl,
    name = name,
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

internal fun NetworkItem.toRepositoryEntity() = RepositoryEntity(
    repoId = id,
    userId = owner.id,
    avatarUrl = owner.avatarUrl,
    name = name,
    nodeId = nodeId,
    forks = forks,
    watchersCount = watchersCount,
    createdAt = dateTimeConverter(createdAt),
    updatedAt = dateTimeConverter(updatedAt),
    pushedAt = dateTimeConverter(pushedAt),
    defaultBranch = defaultBranch,
    stargazersCount = stargazersCount,
    description = description,
    tagsUrl = tagsUrl,
    branchesUrl = branchesUrl,
    commitsUrl = commitsUrl,
    topics = topics,
    licence = networkLicense?.toLicenseEntity()
)

internal fun NetworkLicense.toLicenseEntity() = License(
    key = key,
    url = url
)

internal fun NetworkItem.toExternalModel() = Repository(
    id = id,
    userId = owner.id,
    avatarUrl = owner.avatarUrl,
    name = name,
    nodeId = nodeId,
    forks = forks,
    watchersCount = watchersCount,
    createdAt = dateTimeConverter(createdAt),
    updatedAt = dateTimeConverter(updatedAt),
    pushedAt = dateTimeConverter(pushedAt),
    defaultBranch = defaultBranch,
    stargazersCount = stargazersCount,
    description = description,
    tagsUrl = tagsUrl,
    branchesUrl = branchesUrl,
    commitsUrl = commitsUrl,
    topics = topics,
    license = networkLicense?.toLicenseEntity()
)

internal fun List<NetworkItem>.toRepositoryEntity() = map { it.toRepositoryEntity() }

internal fun List<NetworkItem>.toExternalModel() = map { it.toExternalModel() }
