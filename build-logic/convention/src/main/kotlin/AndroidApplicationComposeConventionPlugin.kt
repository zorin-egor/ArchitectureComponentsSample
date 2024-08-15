import com.android.build.api.dsl.ApplicationExtension
import com.sample.architecturecomponents.configureAndroidCompose
import com.sample.architecturecomponents.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply(libs.findPlugin("compose-compiler").get().get().pluginId)

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}
