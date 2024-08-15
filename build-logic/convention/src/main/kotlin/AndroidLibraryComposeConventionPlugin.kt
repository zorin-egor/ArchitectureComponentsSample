import com.android.build.gradle.LibraryExtension
import com.sample.architecturecomponents.configureAndroidCompose
import com.sample.architecturecomponents.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply(libs.findPlugin("compose-compiler").get().get().pluginId)
            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}
