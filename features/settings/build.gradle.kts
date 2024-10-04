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
    implementation(projects.core.notifications)
    
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(projects.core.testing)
}
