plugins {
    alias(libs.plugins.sample.feature)
    alias(libs.plugins.sample.android.library.compose)
}

android {
    namespace = "com.sample.architecturecomponents.feature.repositories"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(libs.bundles.coil)
    implementation(libs.androidx.compose.constraint.layout)

    testImplementation(libs.androidx.test.core)
    testImplementation(projects.core.testing)
}
