package com.sample.architecturecomponents.core.model

data class Repository(
    val id: Long,
    val userId: Long,
    val avatarUrl: String?,
    val name: String,
    val nodeId: String,
    val forks: Int,
    val watchersCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val pushedAt: String,
    val defaultBranch: String,
    val stargazersCount: Int,
    val description: String?,
    val tagsUrl: String,
    val branchesUrl: String,
    val commitsUrl: String,
    val topics: List<String>,
    val license: License?
)
