package com.sample.architecturecomponents.core.model

data class SettingsData(
    val cacheSize: String,
    val version: String,
    val preference: SettingsPreferenceData,
)
