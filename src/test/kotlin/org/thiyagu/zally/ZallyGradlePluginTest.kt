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
                           id("io.github.thiyagu06")
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
        project.pluginManager.apply("io.github.thiyagu06")

        assertTrue(project.tasks.getByName("zallyLint") is ZallyLintTask)
    }

    @Test
    fun `should fail when the input spec is not provided`(@TempDir tempDir: File) {
        File(tempDir, "build.gradle.kts").run {
            writeText(
                """
                        plugins {
                           id("io.github.thiyagu06")
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
                           id("io.github.thiyagu06")
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
                           id("io.github.thiyagu06")
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

    @Test
    fun `should print violation in html file when enabled`(@TempDir tempDir: File) {
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
                           id("io.github.thiyagu06")
                        }
                        zallyLint {
                            inputSpec = File("${tempDir}/notitle.yml")
                            reports{
                                html {
                                    enabled = true
                                    destination = File("${tempDir}/violation.html")
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
        val violationFile = File(tempDir, "violation.html")
        assertTrue { violationFile.exists() }
        assertThat(buildResult.task(":zallyLint")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }

    @Test
    fun `should print violation in both json and html file when enabled`(@TempDir tempDir: File) {
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
                           id("io.github.thiyagu06")
                        }
                        zallyLint {
                            inputSpec = File("${tempDir}/notitle.yml")
                            ignoredRules = "M101,M101"
                            reports{
                                html {
                                    enabled = true
                                    destination = File("${tempDir}/violation.html")
                                }
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
        val violationsHtmlFile = File(tempDir, "violation.html")
        assertTrue { violationsHtmlFile.exists() }
        val violationsJsonFile = File(tempDir, "violation.json")
        assertTrue { violationsJsonFile.exists() }
        assertThat(buildResult.task(":zallyLint")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }

    @Test
    fun `should fail the build when violation rules passed`(@TempDir tempDir: File) {
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
                           id("io.github.thiyagu06")
                        }
                        zallyLint {
                            inputSpec = File("${tempDir}/notitle.yml")
                            ignoredRules = "M101,M101"
                            rules {
                                should {
                                    max = 50
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
        println(buildResult.output)
        assertThat(buildResult.task(":zallyLint")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(buildResult.output)
            .doesNotContain("spec has violation which must be fixed. see console for more info")
    }

    @Test
    fun `should not fail the build when violation rules is not passed`(@TempDir tempDir: File) {
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
                           id("io.github.thiyagu06")
                        }
                        zallyLint {
                            inputSpec = File("${tempDir}/notitle.yml")
                            ignoredRules = "M101,M101"
                            reports{
                                html {
                                    enabled = true
                                    destination = File("${tempDir}/violation.html")
                                }
                                json {
                                    enabled = true
                                    destination = File("${tempDir}/violation.json")
                                }
                            }
                            rules {
                                should {
                                    max = 50
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
        assertThat(buildResult.task(":zallyLint")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(buildResult.output)
            .doesNotContain("spec has violation which must be fixed. see console for more info")
    }
}
