package org.thiyagu.zally

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.thiyagu.zally.internal.ZallyLintTask
import java.io.File
import kotlin.test.Ignore
import kotlin.test.assertTrue

@Ignore
class ZallyGradlePluginTest {

    @Test
    fun `plugin should successfully execute the task`(@TempDir tempDir: File) {
        val spec = """
    swagger: "2.0"
    info:
      description: Test Description
      version: "1.0.0"
      contact:
        name: John Smith
        email: smith@example.com
    paths:
      /articles:
        get:
          summary: returns list of articles
          responses:
            200:
              description: Success
            """.trimIndent()
        File(tempDir, "build.gradle.kts").run {
            writeText(
                """
                        plugins {
                           id("org.thiyagu.zally")
                        }
                        zallyLint {
                            inputSpec = File("${tempDir}/notitle.yml")
                        }
                        """
            )
        }
        File(tempDir, "notitle.yml").apply {
            parentFile.mkdirs()
            createNewFile()
            appendText(spec)
        }
        val buildResult = GradleRunner.create()
            .withProjectDir(tempDir)
            .withPluginClasspath()
            .withArguments("zallyLint")
            .build()
        assertThat(buildResult.task(":zallyLint")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }

    @Test
    fun `should successfully register the task`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("org.thiyagu.zally")

        assertTrue(project.tasks.getByName("zallyLint") is ZallyLintTask)
    }

    @Test
    fun `should fail when the input spec is not provided`(@TempDir tempDir: File) {
        File(tempDir, "build.gradle.kts").run {
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
        assertThat(buildResult.output).contains("input spec should be provided")
    }

    @Test
    fun `should print violation in console by default`(@TempDir tempDir: File) {
        val spec = """
    swagger: "2.0"
    info:
      description: Test Description
      version: "1.0.0"
      contact:
        name: John Smith
        email: smith@example.com
    paths:
      /articles:
        get:
          summary: returns list of articles
          responses:
            200:
              description: Success
            """.trimIndent()
        File(tempDir, "build.gradle.kts").run {
            writeText(
                """
                        plugins {
                           id("org.thiyagu.zally")
                        }
                        zallyLint {
                            inputSpec = File("${tempDir}/notitle.yml")
                        }
                        """
            )
        }
        File(tempDir, "notitle.yml").apply {
            parentFile.mkdirs()
            createNewFile()
            appendText(spec)
        }
        val buildResult = GradleRunner.create()
            .withProjectDir(tempDir)
            .withPluginClasspath()
            .withArguments("zallyLint")
            .build()
        assertThat(buildResult.task(":zallyLint")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(buildResult.output).contains("SEVERITY COUNT")
    }

    @Test
    fun `should print violation in json file when enabled`(@TempDir tempDir: File) {
        val spec = """
    swagger: "2.0"
    info:
      description: Test Description
      version: "1.0.0"
      contact:
        name: John Smith
        email: smith@example.com
    paths:
      /articles:
        get:
          summary: returns list of articles
          responses:
            200:
              description: Success
            """.trimIndent()
        File(tempDir, "build.gradle.kts").run {
            writeText(
                """
                        plugins {
                           id("org.thiyagu.zally")
                        }
                        zallyLint {
                            inputSpec = File("${tempDir}/notitle.yml")
                            reports{
                                json {
                                    enabled = true
                                    destination = File("${tempDir}/violation.json")
                                }
                            }
                        }
                        """
            )
        }
        File(tempDir, "notitle.yml").apply {
            parentFile.mkdirs()
            createNewFile()
            appendText(spec)
        }
        val buildResult = GradleRunner.create()
            .withProjectDir(tempDir)
            .withPluginClasspath()
            .withArguments("zallyLint")
            .build()
        val violationFile = File(tempDir, "violation.json")
        assertTrue { violationFile.exists() }
        assertThat(buildResult.task(":zallyLint")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }
}
