plugins {
    alias(libs.plugins.sample.android.application)
    alias(libs.plugins.sample.android.application.compose)
    alias(libs.plugins.sample.android.application.flavors)
    alias(libs.plugins.sample.hilt)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

val appCode = 1
val appVersion = "0.0.1"

android {
    namespace = "com.sample.architecturecomponents.app"

    defaultConfig {
        applicationId = "com.sample.architecturecomponents"
        versionCode = appCode
        versionName = appVersion

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "APP_NAME", "\"ArchitectureComponents\"")
        buildConfigField("String", "VERSION_NAME", "\"$appVersion\"")
        buildConfigField("Integer", "VERSION_CODE", "$appCode")
    }

    buildTypes {
        debug {
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    buildFeatures{
        buildConfig = true
    }
}

secrets {
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    implementation(projects.features.users)
    implementation(projects.features.userDetails)
    implementation(projects.features.themes)
    implementation(projects.features.settings)
    implementation(projects.features.repositories)
    implementation(projects.features.repositoryDetails)

    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
    implementation(projects.core.model)

    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.kt)

    ksp(libs.hilt.compiler)
}
