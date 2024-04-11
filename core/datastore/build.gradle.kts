plugins {
    alias(libs.plugins.sample.android.library)
    alias(libs.plugins.sample.hilt)
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    namespace = "com.sample.architecturecomponents.core.datastore"
}

dependencies {
    api(libs.androidx.dataStore.core)
    api(libs.androidx.dataStore.preference)
    api(projects.core.datastoreProto)
    api(projects.core.model)
    implementation(projects.core.common)
}
