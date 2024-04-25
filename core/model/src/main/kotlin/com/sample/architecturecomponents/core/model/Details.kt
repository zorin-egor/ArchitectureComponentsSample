package com.sample.architecturecomponents.core.model

data class Details(
    val id: Long,
    val url: String,
    val avatarUrl: String?,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    val publicRepos: Long?,
    val publicGists: Long?,
    val followers: Long?,
    val following: Long?,
    val createdAt: String?,
    val reposUrl: String?
)