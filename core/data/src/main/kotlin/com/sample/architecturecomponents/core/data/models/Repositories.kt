package com.sample.architecturecomponents.core.data.models

import com.sample.architecturecomponents.core.database.model.RepositoryEntity
import com.sample.architecturecomponents.core.model.License
import com.sample.architecturecomponents.core.model.Repository
import com.sample.architecturecomponents.core.network.converters.dateTimeConverter
import com.sample.architecturecomponents.core.network.models.common.NetworkLicense
import com.sample.architecturecomponents.core.network.models.common.NetworkRepository

internal fun Repository.toRepositoryEntity() = RepositoryEntity(
    repoId = id,
    userId = userId,
    owner = owner,
    avatarUrl = avatarUrl,
    name = name,
    forks = forks,
    watchersCount = watchersCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
    stargazersCount = stargazersCount,
    description = description
)

internal fun NetworkRepository.toRepositoryEntity() = RepositoryEntity(
    repoId = id,
    userId = owner.id,
    owner = owner.login,
    avatarUrl = owner.avatarUrl,
    name = name,
    forks = forks,
    watchersCount = watchersCount,
    createdAt = dateTimeConverter(createdAt),
    updatedAt = dateTimeConverter(updatedAt),
    stargazersCount = stargazersCount,
    description = description
)

internal fun NetworkLicense.toLicenseEntity() = License(
    key = key,
    url = url
)

internal fun NetworkRepository.toRepositoryModel() = Repository(
    id = id,
    userId = owner.id,
    owner = owner.login,
    avatarUrl = owner.avatarUrl,
    name = name,
    forks = forks,
    watchersCount = watchersCount,
    createdAt = dateTimeConverter(createdAt),
    updatedAt = dateTimeConverter(updatedAt),
    stargazersCount = stargazersCount,
    description = description
)

internal fun List<NetworkRepository>.toRepositoryEntities(): List<RepositoryEntity> = map { it.toRepositoryEntity() }

internal fun List<NetworkRepository>.toRepositoryModels(): List<Repository> = map { it.toRepositoryModel() }
