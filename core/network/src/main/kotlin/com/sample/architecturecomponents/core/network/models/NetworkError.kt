package com.sample.architecturecomponents.core.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkError(
    @SerialName("message") val error: String,
    @SerialName("documentation_url") val documentationUrl: String,
)