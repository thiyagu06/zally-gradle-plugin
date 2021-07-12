package org.thiyagu.zally

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.thiyagu.zally.internal.ZallyLintTask
import java.io.File
import kotlin.test.assertTrue

class ZallyGradlePluginTest {

    @Test
    fun `plugin should successfully execute the task`(@TempDir tempDir: File) {
        File(tempDir, "build.gradle").run {
            writeText(
                """
                        plugins {
                           id("org.thiyagu.zally")
                        }
                        zallyLint {
                            inputSpecUrl = "https://raw.githubusercontent.com/zalando/zally/master/examples/sample_swagger_api.yaml"
                        }
                        """
            )
        }

        val buildResult = GradleRunner.create()
            .withProjectDir(tempDir)
            .withPluginClasspath()
            .withArguments("zallyLint")
            .build()
       assertThat(buildResult.task(":zallyLint")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }

    @Test
    fun `plugin should successfully register the task`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("org.thiyagu.zally")

        assertTrue(project.tasks.getByName("zallyLint") is ZallyLintTask)
    }

    @Test
    fun `zallyLint task should fail when the input spec is not provided`(@TempDir tempDir: File) {
        File(tempDir, "build.gradle").run {
            writeText(
                """
                        plugins {
                           id("org.thiyagu.zally")
                        }
                        zallyLint {
                        }
                        """
            )
        }

        val buildResult = GradleRunner.create()
            .withProjectDir(tempDir)
            .withPluginClasspath()
            .withArguments("zallyLint")
            .buildAndFail()
        assertThat(buildResult.task(":zallyLint")!!.outcome).isEqualTo(TaskOutcome.FAILED)
    }
}
