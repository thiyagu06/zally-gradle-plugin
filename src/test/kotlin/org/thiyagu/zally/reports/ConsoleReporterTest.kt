package org.thiyagu.zally.reports

import com.fasterxml.jackson.core.JsonPointer
import org.assertj.core.api.Assertions.assertThat
import org.gradle.internal.impldep.org.junit.After
import org.gradle.internal.impldep.org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.zalando.zally.core.Result
import org.zalando.zally.rule.api.Severity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.net.URI
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse


class ConsoleReporterTest {

    private val outContent: ByteArrayOutputStream = ByteArrayOutputStream()
    private val errContent: ByteArrayOutputStream = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalErr = System.err

    @BeforeEach
    fun setUpStreams() {
        System.setOut(PrintStream(outContent))
        System.setErr(PrintStream(errContent))
    }

    @AfterEach
    fun restoreStreams() {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    @Test
    fun `should print all violations`() {
        val shouldViolation = Result(
            "M011",
            URI("https://zalando.github.io/restful-api-guidelines/#219"),
            "Provide API Audience",
            "API Audience must be provided",
            Severity.SHOULD,
            JsonPointer.empty(),
            null,
        )
        val mustViolation = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.MUST,
            JsonPointer.empty(),
            null
        )
        val mayViolation = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.MAY,
            JsonPointer.empty(),
            null
        )
        val hintViolation = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.HINT,
            JsonPointer.empty(),
            null
        )

        val violations = listOf(shouldViolation, mustViolation, hintViolation, mayViolation)
        ConsoleReporter().render(violations)
        val consoleContents = outContent.toString()
        assertThat(consoleContents).contains(Severity.MUST.name)
        assertThat(consoleContents).contains(Severity.MAY.name)
        assertThat(consoleContents).contains(Severity.SHOULD.name)
        assertThat(consoleContents).contains(Severity.HINT.name)
    }

    @Test
    fun `should print all violations when invoking the write method`() {
        val shouldViolation = Result(
            "M011",
            URI("https://zalando.github.io/restful-api-guidelines/#219"),
            "Provide API Audience",
            "API Audience must be provided",
            Severity.SHOULD,
            JsonPointer.empty(),
            null,
        )
        val mustViolation = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.MUST,
            JsonPointer.empty(),
            null
        )
        val mayViolation = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.MAY,
            JsonPointer.empty(),
            null
        )
        val hintViolation = Result(
            "M011",
            URI("https://github.com/zalando/zally/blob/master/server/rules.md#m011-tag-all-operations"),
            "Tag all operations",
            "Operation has no tag",
            Severity.HINT,
            JsonPointer.empty(),
            null
        )

        val violations = listOf(shouldViolation, mustViolation, hintViolation, mayViolation)
        ConsoleReporter().write(violations, File("violation.json").toPath())
        val consoleContents = outContent.toString()
        assertThat(consoleContents).contains(Severity.MUST.name)
        assertThat(consoleContents).contains(Severity.MAY.name)
        assertThat(consoleContents).contains(Severity.SHOULD.name)
        assertThat(consoleContents).contains(Severity.HINT.name)
    }

    @Test
    fun `should print success message violations is empty`() {
        ConsoleReporter().write(emptyList(), File("violation.json").toPath())
        val consoleContents = outContent.toString()
        assertThat(consoleContents).contains("No violation identified in the spec. Great stuff!! \\U+1F389")
    }
}
