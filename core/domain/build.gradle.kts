plugins {
    alias(libs.plugins.sample.android.library)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.sample.architecturecomponents.core.domain"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)
    implementation(libs.javax.inject)
}