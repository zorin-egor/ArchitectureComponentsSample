package com.sample.architecturecomponents.lint

import com.android.tools.lint.checks.AllowAllHostnameVerifierDetector
import com.android.tools.lint.checks.BatteryDetector
import com.android.tools.lint.checks.FirebaseAnalyticsDetector
import com.android.tools.lint.checks.FirebaseMessagingDetector
import com.android.tools.lint.checks.GradleDetector
import com.android.tools.lint.checks.ManifestDetector
import com.android.tools.lint.checks.SplashScreenDetector
import com.android.tools.lint.checks.UnusedResourceDetector
import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.sample.architecturecomponents.lint.detectors.DesignSystemDetector
import com.sample.architecturecomponents.lint.detectors.GradlePluginModuleDetector
import com.sample.architecturecomponents.lint.detectors.TestMethodNameDetector

class AppIssueRegistry : IssueRegistry() {

    override val issues = listOf(
        GradleDetector.SWITCH_TO_TOML,
        DesignSystemDetector.ISSUE,
        ManifestDetector.ORDER,
        SplashScreenDetector.ISSUE,
        AllowAllHostnameVerifierDetector.ISSUE,
        UnusedResourceDetector.ISSUE,
        UnusedResourceDetector.ISSUE_IDS,
        BatteryDetector.ISSUE,
        FirebaseAnalyticsDetector.INVALID_NAME,
        FirebaseMessagingDetector.MISSING_TOKEN_REFRESH,
        TestMethodNameDetector.FORMAT,
        TestMethodNameDetector.PREFIX,
        GradlePluginModuleDetector.MODULES_CONVENTION_PLUGINS
    )

    override val api: Int = CURRENT_API

    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "Architecture components sample app",
        contact = "https://github.com/zorin-egor/ArchitectureComponentsSample",
    )
}
