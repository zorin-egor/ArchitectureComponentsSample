plugins {
    alias(libs.plugins.sample.android.library)
    alias(libs.plugins.sample.hilt)
}

android {
    namespace = "com.sample.architecturecomponents.core.data"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    lintChecks(projects.lint)
    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.datastore)
    api(projects.core.network)
    implementation(projects.core.model)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(projects.core.testing)
}