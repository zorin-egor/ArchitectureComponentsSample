package com.sample.architecturecomponents.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sample.architecturecomponents.core.model.Repository
import kotlinx.datetime.Instant

@Entity(
    tableName = "Repositories",
    indices = [
        Index(value = ["repo_id"], unique = true),
        Index(value = ["user_id"]),
        Index(value = ["name"])
    ]
)
data class RepositoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "repo_id") val repoId: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "owner") val owner: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "forks") val forks: Int,
    @ColumnInfo(name = "watchers_count") val watchersCount: Int,
    @ColumnInfo(name = "created_at") val createdAt: Instant,
    @ColumnInfo(name = "updated_at") val updatedAt: Instant,
    @ColumnInfo(name = "stargazers_count") val stargazersCount: Int,
    @ColumnInfo(name = "description") val description: String?,
)

fun RepositoryEntity.asExternalModel() = Repository(
    id = repoId,
    userId = userId,
    owner = owner,
    name = name,
    avatarUrl = avatarUrl,
    forks = forks,
    watchersCount = watchersCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
    stargazersCount = stargazersCount,
    description = description
)

fun List<RepositoryEntity>.asExternalModel(): List<Repository> = map { it.asExternalModel() }