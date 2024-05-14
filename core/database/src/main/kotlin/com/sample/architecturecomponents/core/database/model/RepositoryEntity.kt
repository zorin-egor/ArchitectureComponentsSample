package com.sample.architecturecomponents.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sample.architecturecomponents.core.model.License
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
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "node_id") val nodeId: String,
    @ColumnInfo(name = "forks") val forks: Int,
    @ColumnInfo(name = "watchers_count") val watchersCount: Int,
    @ColumnInfo(name = "created_at") val createdAt: Instant,
    @ColumnInfo(name = "updated_at") val updatedAt: Instant,
    @ColumnInfo(name = "pushed_at") val pushedAt: Instant,
    @ColumnInfo(name = "default_branch") val defaultBranch: String,
    @ColumnInfo(name = "stargazers_count") val stargazersCount: Int,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "tags_url") val tagsUrl: String,
    @ColumnInfo(name = "branches_url") val branchesUrl: String,
    @ColumnInfo(name = "commits_url") val commitsUrl: String,
    @ColumnInfo(name = "topics") val topics: List<String>,
    @ColumnInfo(name = "license") val licence: License?,
)

fun RepositoryEntity.asExternalModel() = Repository(
    id = repoId,
    userId = userId,
    name = name,
    nodeId = nodeId,
    avatarUrl = avatarUrl,
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
    license = licence
)

fun List<RepositoryEntity>.asExternalModel() = map { it.asExternalModel() }