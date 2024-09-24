plugins {
    alias(libs.plugins.sample.android.library)
    alias(libs.plugins.sample.hilt)
}

android {
    namespace = "com.sample.architecturecomponents.core.datastore"

    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    api(libs.androidx.dataStore.core)
    api(libs.androidx.dataStore.preference)
    api(projects.core.datastoreProto)
    api(projects.core.model)

    implementation(projects.core.common)
    implementation(libs.androidx.test.rules)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.runner)

    testImplementation(libs.kotlinx.coroutines.test)
}
