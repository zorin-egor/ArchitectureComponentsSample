plugins {
    alias(libs.plugins.sample.android.library)
    alias(libs.plugins.sample.hilt)
}

android {
    namespace = "com.sample.architecturecomponents.core.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}