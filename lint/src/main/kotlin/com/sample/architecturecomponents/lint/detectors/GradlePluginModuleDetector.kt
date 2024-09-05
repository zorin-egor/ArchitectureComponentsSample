package com.sample.architecturecomponents.lint.detectors

import com.android.tools.lint.checks.VC_PLUGINS
import com.android.tools.lint.checks.VC_VERSIONS
import com.android.tools.lint.checks.getPluginFromTomlEntry
import com.android.tools.lint.client.api.LintTomlMapValue
import com.android.tools.lint.client.api.LintTomlValue
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.GradleContext
import com.android.tools.lint.detector.api.GradleContext.Companion.getStringLiteralValue
import com.android.tools.lint.detector.api.GradleScanner
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity

class GradlePluginModuleDetector : Detector(), GradleScanner {

    override fun checkDslPropertyAssignment(
        context: GradleContext,
        property: String,
        value: String,
        parent: String,
        parentParent: String?,
        propertyCookie: Any,
        valueCookie: Any,
        statementCookie: Any,
    ) {
        println("checkDslPropertyAssignment($parent, $property, $value)")

        if (parent == "plugins") {
            val plugin = when (property) {
                "id" -> getStringLiteralValue(value, valueCookie)
                "alias" -> getPluginFromVersionCatalog(value, context)?.coordinates?.substringBefore(':')
                else -> return
            }

            println("checkDslPropertyAssignment() - plugin:  $plugin")

            val valueInstead = PLUGINS_MAPPING[plugin]
            if (PLUGINS_MAPPING.containsKey(plugin)) {
                context.report(
                    MODULES_CONVENTION_PLUGINS,
                    context.getLocation(propertyCookie),
                    "Use plugin $valueInstead, instead plugin $plugin"
                )
            }
        }
    }

    private data class VersionCatalogDependency(
        val coordinates: String,
        val tomlValue: LintTomlValue,
    )

    private fun getPluginFromVersionCatalog(
        expression: String,
        context: GradleContext,
    ): VersionCatalogDependency? {
        if (!expression.startsWith(PLUGIN_PREFIX)) return null

        val pluginName = expression.substring(PLUGIN_PREFIX.length)
        val plugin = (context.getTomlValue(VC_PLUGINS) as? LintTomlMapValue)
                ?.getMappedValues()
                ?.asIterable()
                ?.find { it.key.replace('-', '.').replace('_', '.') == pluginName }
                ?.value ?: return null

        val versions = context.getTomlValue(VC_VERSIONS) as? LintTomlMapValue
        val (coordinate, _) = getPluginFromTomlEntry(versions, plugin) ?: return null

        return VersionCatalogDependency(coordinate, plugin)
    }

    companion object {
        private const val PLUGIN_PREFIX = "libs.plugins."

        private val PLUGINS_MAPPING = mapOf(
            "com.android.application" to "libs.plugins.sample.android.application",
            "com.android.library" to "libs.plugins.sample.android.library",
            "org.jetbrains.kotlin.jvm" to "libs.plugins.sample.jvm",
        )

        private val GRADLE_CONVENTION_PLUGINS =
            Implementation(
                GradlePluginModuleDetector::class.java,
                Scope.GRADLE_SCOPE
            )

        @JvmField
        val MODULES_CONVENTION_PLUGINS = Issue.create(
            id = "UseConventionPlugin",
            briefDescription = "Use convention plugins",
            explanation = "Plugins from the convention module must be used",
            category = Category.PRODUCTIVITY,
            priority = 1,
            severity = Severity.ERROR,
            implementation = GRADLE_CONVENTION_PLUGINS,
        )

    }
}
