package com.sample.architecturecomponents.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sample.architecturecomponents.core.model.License
import com.sample.architecturecomponents.core.model.RepositoryDetails
import kotlinx.datetime.Instant

@Entity(
    tableName = "RepositoryDetails",
    indices = [
        Index(value = ["repo_id", "user_id"], unique = true),
        Index(value = ["owner"]),
        Index(value = ["name"]),
    ]
)
data class RepositoryDetailsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "repo_id") val repoId: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "owner") val owner: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "html_url") val htmlUrl: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "node_id") val nodeId: String,
    @ColumnInfo(name = "forks") val forks: Int,
    @ColumnInfo(name = "watchers_count") val watchersCount: Int,
    @ColumnInfo(name = "created_at") val createdAt: Instant,
    @ColumnInfo(name = "updated_at") val updatedAt: Instant,
    @ColumnInfo(name = "pushed_at", defaultValue = "'NULL'") val pushedAt: Instant?,
    @ColumnInfo(name = "default_branch") val defaultBranch: String,
    @ColumnInfo(name = "stargazers_count") val stargazersCount: Int,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "tags_url") val tagsUrl: String,
    @ColumnInfo(name = "branches_url") val branchesUrl: String,
    @ColumnInfo(name = "commits_url") val commitsUrl: String,
    @ColumnInfo(name = "topics") val topics: List<String>,
    @ColumnInfo(name = "license") val licence: License?,
)

fun RepositoryDetailsEntity.asExternalModels() = RepositoryDetails(
    id = repoId,
    userId = userId,
    userLogin = owner,
    name = name,
    htmlUrl = htmlUrl,
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

fun List<RepositoryDetailsEntity>.asExternalModels(): List<RepositoryDetails> = map { it.asExternalModels() }