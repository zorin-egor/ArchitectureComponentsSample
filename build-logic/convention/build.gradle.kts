import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.sample.architecturecomponents.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.tools.build.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.kotlin.compose.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("sampleAndroidApplication") {
            id = "sample.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("sampleAndroidApplicationCompose") {
            id = "sample.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("sampleAndroidFlavors") {
            id = "sample.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
        register("sampleAndroidLibrary") {
            id = "sample.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("sampleCompose") {
            id = "sample.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("sampleHilt") {
            id = "sample.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("sampleRoom") {
            id = "sample.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("sampleFeature") {
            id = "sample.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidLint") {
            id = "sample.android.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("sampleJvm") {
            id = "sample.jvm"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
