package com.sample.architecturecomponents.core.network.models


import com.sample.architecturecomponents.core.network.models.common.NetworkItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRepositories(
    @SerialName("incomplete_results") val incompleteResults: Boolean,
    @SerialName("items") val networkItems: List<NetworkItem>,
    @SerialName("total_count") val totalCount: Int
)