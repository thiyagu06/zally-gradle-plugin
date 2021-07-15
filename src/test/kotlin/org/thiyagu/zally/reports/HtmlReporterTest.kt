package org.thiyagu.zally.reports

import com.fasterxml.jackson.core.JsonPointer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.zalando.zally.core.Result
import org.zalando.zally.rule.api.Severity
import java.io.File
import java.net.URI
import kotlin.test.assertFalse

class HtmlReporterTest {

    @Test
    fun `should contain all violations in the html`() {
        val shouldViolation = Result(
            "M011",
            URI("https://zalando.github.io/restful-api-guidelines/#219"),
            "Provide API Audience",
            "API Audience must be provided",
            Severity.SHOULD,
            JsonPointer.valueOf("/dadadadadÖANdland ,ad adadalada"),
            1..5,
        )
        val mustViolation = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.MUST,
            JsonPointer.empty(),
            5..7
        )
        val mayViolation = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.MAY,
            JsonPointer.empty(),
            7..8
        )
        val hintViolation = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.HINT,
            JsonPointer.empty(),
            null,
        )

        val violations = listOf(shouldViolation, mustViolation, hintViolation, mayViolation)
        val result = HtmlReporter().render(violations)
        assertThat(result).contains("<td>MUST</td>")
        assertThat(result).contains("<td>Operation has no tag</td>")
        assertThat(result).contains("<table id=\"findings\">")
        assertThat(result).contains("<table id=\"summary\">")
        assertThat(result).contains("<td>N/A</td>")
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
        val violationFile = File(tempDir, "violation.html")
        HtmlReporter().write(listOf(tagAllViolaton), tempDir.toPath())
        assertFalse { violationFile.exists() }
    }

    @Test
    fun `violation file should not be created if violation is empty`(@TempDir tempDir: File) {
        val violationFile = File(tempDir, "violation.html")
        HtmlReporter().write(emptyList(), violationFile.toPath())
        assertFalse { violationFile.exists()}
    }
}
