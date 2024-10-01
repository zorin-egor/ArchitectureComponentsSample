package com.sample.architecturecomponents.core.data.models

import com.sample.architecturecomponents.core.database.model.UserEntity
import com.sample.architecturecomponents.core.model.User
import com.sample.architecturecomponents.core.network.models.NetworkUser

internal fun User.toUserEntity() = UserEntity(
    userId = id,
    nodeId = nodeId,
    login = login,
    url = url,
    avatarUrl = avatarUrl,
)

internal fun NetworkUser.toUserEntity() = UserEntity(
    userId = id,
    nodeId = nodeId,
    login = login,
    url = url,
    avatarUrl = avatarUrl,
)

internal fun List<NetworkUser>.toUserEntity() = map { it.toUserEntity() }

internal fun NetworkUser.toUserModel() = User(
    id = id,
    nodeId = nodeId,
    login = login,
    url = url,
    avatarUrl = avatarUrl,
)

internal fun List<NetworkUser>.toUserModels() = map { it.toUserModel() }