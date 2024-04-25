plugins {
    alias(libs.plugins.sample.feature)
    alias(libs.plugins.sample.android.library.compose)
}

android {
    namespace = "com.sample.architecturecomponents.feature.themes"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(projects.core.data)
}
