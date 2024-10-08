plugins {
    alias(libs.plugins.sample.android.library)
    alias(libs.plugins.sample.hilt)
}

android {
    namespace = "com.sample.architecturecomponents.core.testing.tests"
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(projects.core.data)
    api(projects.core.notifications)

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
}
