plugins {
    alias(libs.plugins.sample.android.library)
    alias(libs.plugins.sample.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.sample.architecturecomponents.core.network"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(libs.bundles.network)
    api(projects.core.common)
    api(projects.core.model)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.datastore)
}