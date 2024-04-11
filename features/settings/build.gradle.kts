plugins {
    alias(libs.plugins.sample.feature)
    alias(libs.plugins.sample.android.library.compose)
}

android {
    namespace = "com.sample.architecturecomponents.feature.settings"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(projects.core.data)
}
