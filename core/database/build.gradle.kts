plugins {
    alias(libs.plugins.sample.android.library)
    alias(libs.plugins.sample.hilt)
    alias(libs.plugins.sample.room)
}

android {
    namespace = "com.sample.architecturecomponents.core.database"
}

dependencies {
    api(projects.core.model)
    implementation(libs.kotlinx.datetime)
    implementation(libs.square.retrofit2.gson.converter)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
