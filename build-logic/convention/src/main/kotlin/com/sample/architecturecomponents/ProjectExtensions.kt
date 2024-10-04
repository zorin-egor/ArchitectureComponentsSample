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

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Properties.setProperties(path: String, onNotExist: (() -> Unit)? = null) {
    val propertiesFile = File(path)
    if (propertiesFile.exists()) {
        load(FileInputStream(propertiesFile))
    } else {
        onNotExist?.invoke()
        val writer = FileWriter(propertiesFile, false)
        store(writer, null)
    }
}

fun CommonExtension<*, *, *, *, *, *>.createSigningConfig(
    name: String,
    propertiesPath: String,
    keystorePath: String,
    onPropertiesNotExist: (SigningConfig.() -> Unit)? = null
) {
    signingConfigs {
        create(name) {
            val properties = Properties()
            properties[::keyPassword.name] = ""
            properties[::keyAlias.name] = ""
            properties[::storePassword.name] = ""
            properties.setProperties(propertiesPath) {
                onPropertiesNotExist?.invoke(this)
            }

            storeFile = File(keystorePath)
            keyPassword = properties[::keyPassword.name].toString()
            keyAlias = properties[::keyAlias.name].toString()
            storePassword = properties[::storePassword.name].toString()
        }
    }
}
