package org.thiyagu.zally.internal

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.GREEN_TEXT
import com.diogonunes.jcolor.Attribute.BOLD
import com.diogonunes.jcolor.Attribute.BRIGHT_MAGENTA_TEXT
import com.diogonunes.jcolor.Attribute.RED_TEXT
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.thiyagu.zally.reports.ConsoleReporter
import org.thiyagu.zally.reports.ReportFactory
import java.io.File

open class ZallyLintTask : DefaultTask() {

    @OutputDirectory
    var defaultReportDirectory: File = project.file("${project.buildDir}/zally")

    @TaskAction
    fun action() {
        val userConfig = project.extensions.run {
            findByName("zallyLint") as ZallyLint
        }
        with(userConfig) {
            val specContent =
                inputSpec?.bufferedReader()?.readText()
                    ?: throw IllegalArgumentException("input spec should be provided")
            val ignoredRules = userConfig.ignoredRules?.split(",")?.toList() ?: emptyList()
            val decoratedRulesPolicy = ZallyFactory.rulesPolicy.withMoreIgnores(ignoredRules)
            val violations = ZallyFactory.compositeRulesValidator.validate(specContent, decoratedRulesPolicy)
            if (violations.isNotEmpty()) {
                ConsoleReporter().render(violations)
                reports.enabled().forEach {
                    val reporter = ReportFactory.getReporter(it.reportType)
                    val filePath =
                        it.destination?.toPath() ?: it.reportType.defaultFile(defaultReportDirectory).toPath()
                    reporter.write(violations, filePath)
                }
                val violationsBySeverity = violations.groupingBy { it.violationType }.eachCount()
                if (ZallyFactory.violationRuleCheckers.any { it.check(violationsBySeverity, violationRules) }) {
                    throw GradleException("spec has violation which must be fixed. see console for more info")
                }
            } else {
                println(colorize("No violation identified in the spec. Great stuff!! \\U+1F389", GREEN_TEXT(), BOLD()))
            }
            println(colorize("zally lint has been executed for $inputSpec", BRIGHT_MAGENTA_TEXT(), BOLD()))
        }
    }
}
