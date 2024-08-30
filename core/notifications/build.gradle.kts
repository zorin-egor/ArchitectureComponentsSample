plugins {
    alias(libs.plugins.sample.android.library)
    alias(libs.plugins.sample.hilt)
}

android {
    namespace = "com.sample.architecturecomponents.core.notification"
}

dependencies {
    api(projects.core.model)
    implementation(projects.core.common)
    implementation(libs.bundles.coil)
    compileOnly(platform(libs.androidx.compose.bom))
}
