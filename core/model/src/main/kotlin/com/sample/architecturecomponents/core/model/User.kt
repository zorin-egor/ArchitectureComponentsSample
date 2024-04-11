package com.sample.architecturecomponents.core.model


data class User(
    val id: Long,
    val nodeId: String,
    val login: String,
    val url: String,
    val avatarUrl: String?
)