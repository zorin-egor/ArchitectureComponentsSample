package com.sample.architecturecomponents.lint.detectors

import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.sample.architecturecomponents.lint.detectors.DesignSystemDetector.Companion.ISSUE
import com.sample.architecturecomponents.lint.detectors.DesignSystemDetector.Companion.METHOD_NAMES
import com.sample.architecturecomponents.lint.detectors.DesignSystemDetector.Companion.RECEIVER_NAMES
import org.junit.Test

class DesignSystemDetectorTest {

    @Test
    fun `detect replacements of Composable`() {
        lint()
            .issues(ISSUE)
            .allowMissingSdk()
            .files(
                COMPOSABLE_STUB,
                STUBS,
                @Suppress("LintImplTrimIndent")
                kotlin(
                    """
                    |import androidx.compose.runtime.Composable
                    |
                    |@Composable
                    |fun App() {
                    ${METHOD_NAMES.keys.joinToString("\n") { "|    $it()" }}
                    |}
                    """.trimMargin(),
                ).indented(),
            )
            .run()
            .expect(
                """
                    src/test.kt:5: Error: Use AppTheme name, current name is MaterialTheme  [DesignSystem]
                        MaterialTheme()
                        ~~~~~~~~~~~~~~~
                    src/test.kt:6: Error: Use AppNavigationBar name, current name is NavigationBar  [DesignSystem]
                        NavigationBar()
                        ~~~~~~~~~~~~~~~
                    src/test.kt:7: Error: Use AppNavigationBarItem name, current name is NavigationBarItem  [DesignSystem]
                        NavigationBarItem()
                        ~~~~~~~~~~~~~~~~~~~
                    src/test.kt:8: Error: Use AppNavigationRail name, current name is NavigationRail  [DesignSystem]
                        NavigationRail()
                        ~~~~~~~~~~~~~~~~
                    src/test.kt:9: Error: Use AppNavigationRailItem name, current name is NavigationRailItem  [DesignSystem]
                        NavigationRailItem()
                        ~~~~~~~~~~~~~~~~~~~~
                    src/test.kt:10: Error: Use AppTopBar name, current name is CenterAlignedTopAppBar  [DesignSystem]
                        CenterAlignedTopAppBar()
                        ~~~~~~~~~~~~~~~~~~~~~~~~
                    src/test.kt:11: Error: Use ListContentWidget name, current name is LazyColumn  [DesignSystem]
                        LazyColumn()
                        ~~~~~~~~~~~~
                    7 errors, 0 warnings
               """.trimIndent(),
            )
    }

    @Test
    fun `detect replacements of Receiver`() {
        lint()
            .issues(ISSUE)
            .allowMissingSdk()
            .files(
                COMPOSABLE_STUB,
                STUBS,
                @Suppress("LintImplTrimIndent")
                kotlin(
                    """
                    |fun main() {
                    ${RECEIVER_NAMES.keys.joinToString("\n") { "|    $it.toString()" }}
                    |}
                    """.trimMargin(),
                ).indented(),
            )
            .run()
            .expect(
                """
                    src/test.kt:2: Error: Use AppIcons name, current name is Icons  [DesignSystem]
                        Icons.toString()
                        ~~~~~~~~~~~~~~~~
                    1 errors, 0 warnings
                """.trimIndent(),
            )
    }

    private companion object {

        private val COMPOSABLE_STUB: TestFile = kotlin(
            """
            package androidx.compose.runtime
            annotation class Composable
            """.trimIndent(),
        ).indented()

        private val STUBS: TestFile = kotlin(
            """
            |import androidx.compose.runtime.Composable
            |
            ${METHOD_NAMES.keys.joinToString("\n") { "|@Composable fun $it() = {}" }}
            ${RECEIVER_NAMES.keys.joinToString("\n") { "|object $it" }}
            |
            """.trimMargin(),
        ).indented()
    }
}
