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

    testImplementation(libs.androidx.test.core)
    testImplementation(projects.core.testing)
}
