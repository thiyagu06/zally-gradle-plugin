package org.thiyagu.zally.reports

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.zalando.zally.core.Result
import org.zalando.zally.rule.api.Severity
import java.io.File
import java.net.URI
import kotlin.test.assertFalse

class JsonReporterTest {

    @Test
    fun `should successfully generate json report`(@TempDir tempDir: File) {
        val tagAllViolaton = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.MUST,
            JsonPointer.empty(),
            null
        )
        val provideApiAudienceViolation = Result(
            "M011",
            URI("https://zalando.github.io/restful-api-guidelines/#219"),
            "Provide API Audience",
            "API Audience must be provided",
            Severity.MUST,
            JsonPointer.empty(),
            null,
        )
        val jsonReporter = JsonReporter()
        val violations = listOf(tagAllViolaton, provideApiAudienceViolation)
        val violationFile = File(tempDir, "violation.json")
        jsonReporter.write(violations, violationFile.toPath())
        val violationAsJsonString = ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(violations)
        assertThat(violationFile.readText()).isEqualTo(violationAsJsonString)
    }


    @Test
    fun `should not create violation file when filepath is invalid`(@TempDir tempDir: File) {
        val tagAllViolaton = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.MUST,
            JsonPointer.empty(),
            null
        )
        val jsonReporter = JsonReporter()
        val violations = listOf(tagAllViolaton)
        val violationFile = File(tempDir, "violation.json")
        jsonReporter.write(violations, tempDir.toPath())
        assertFalse { violationFile.exists() }
    }

    @Test
    fun `violation file should not be created not have any when violation is empty`(@TempDir tempDir: File) {
        val jsonReporter = JsonReporter()
        val violations = emptyList<Result>()
        val violationFile = File(tempDir, "violation.json")
        jsonReporter.write(violations, violationFile.toPath())
        assertFalse { violationFile.exists()}
    }
}
