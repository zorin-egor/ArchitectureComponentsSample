package com.sample.architecturecomponents

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.SigningConfig
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.util.Properties

private val String.asSecretForPrint: String
    get() = firstOrNull()?.let { "$it****${last()}"} ?: "-"

private val SigningConfig.isCredentialsInit: Boolean
    get() = storeFile?.exists() == true && !keyAlias.isNullOrEmpty() && !storePassword.isNullOrEmpty()
            && !keyPassword.isNullOrEmpty()

private fun SigningConfig.printSecrets() {
    println("""
        Data for signing: 
            path: ${storeFile?.absolutePath}
            storePwd: ${storePassword?.asSecretForPrint} 
            keyAlias: ${keyAlias?.asSecretForPrint }
            keyPwd: ${keyPassword?.asSecretForPrint }
        """.trimIndent())
}

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Properties.loadOrCreateEmpty(path: String, onNotExist: (() -> Unit)? = null): Boolean {
    val propertiesFile = File(path)
    if (propertiesFile.exists()) {
        load(FileInputStream(propertiesFile))
        return true
    }

    println("Properties file does't exist: $path")
    val writer = FileWriter(propertiesFile, false)
    store(writer, null)
    onNotExist?.invoke()

    return false
}

fun CommonExtension<*, *, *, *, *, *>.createSigningConfig(
    name: String,
    propertiesPath: String,
    keystorePath: String,
    printSignData: Boolean = true,
    onPropertiesNotExist: (SigningConfig.() -> Unit)? = null
) {
    signingConfigs {
        create(name) {
            storeFile = File(keystorePath)

            val properties = Properties()
            properties[::keyPassword.name] = ""
            properties[::keyAlias.name] = ""
            properties[::storePassword.name] = ""

            properties.loadOrCreateEmpty(propertiesPath) {
                onPropertiesNotExist?.invoke(this)
            }

            if (isCredentialsInit) {
                if (printSignData) printSecrets()
                return@create
            }

            keyPassword = properties[::keyPassword.name].toString()
            keyAlias = properties[::keyAlias.name].toString()
            storePassword = properties[::storePassword.name].toString()

            if (printSignData) {
                printSecrets()
            }
        }
    }
}
