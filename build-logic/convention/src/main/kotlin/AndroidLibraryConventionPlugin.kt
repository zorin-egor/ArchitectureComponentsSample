import com.android.build.gradle.LibraryExtension
import com.sample.architecturecomponents.configureFlavors
import com.sample.architecturecomponents.configureKotlinAndroid
import com.sample.architecturecomponents.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("sample.android.lint")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 35
                testOptions.animationsDisabled = true
                configureFlavors(this)
            }

            dependencies {
                add("implementation", libs.findLibrary("timber.log").get())
            }
        }
    }
}
