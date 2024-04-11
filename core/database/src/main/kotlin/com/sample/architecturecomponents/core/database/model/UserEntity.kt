package com.sample.architecturecomponents.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sample.architecturecomponents.core.model.User

@Entity(
    tableName = "Users",
    indices = [
        Index(value = ["user_id"], unique = true),
        Index(value = ["login"])
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "node_id") val nodeId: String,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?
)

fun UserEntity.asExternalModel() = User(
    id = userId,
    nodeId = nodeId,
    login = login,
    url = url,
    avatarUrl = avatarUrl,
)

fun List<UserEntity>.asExternalModel() = map { it.asExternalModel() }