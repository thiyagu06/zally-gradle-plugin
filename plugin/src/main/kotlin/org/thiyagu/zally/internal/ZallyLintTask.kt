package org.thiyagu.zally.internal

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.thiyagu.zally.reports.ReportContext
import org.thiyagu.zally.reports.ReportFactory
import org.thiyagu.zally.reports.ReportFormat.CONSOLE
import java.io.File

open class ZallyLintTask : DefaultTask() {

    @OutputDirectory
    var defaultReportDirectory: File = project.file("${project.buildDir}/zally")


    @TaskAction
    fun action() {
        val input = project.extensions.run {
            findByName("zallyLint") as ZallyExtension
        }
        val specContent =
            input.inputSpec?.let { readSpec(it) } ?: throw IllegalArgumentException("input spec should be provided")
        val decoratedRulesPolicy = ZallyFactory.rulesPolicy.withMoreIgnores(input.ignoredRules.toList())
        val violations = ZallyFactory.compositeRulesValidator.validate(specContent, decoratedRulesPolicy)
        val reportDir = input.reportDir ?: defaultReportDirectory.absolutePath
        val reportContext = ReportContext(reportDir)
        val enabledReportFormats = input.reportFormats.toMutableSet().apply { add(CONSOLE) }
        enabledReportFormats.forEach {
            ReportFactory.getReporter(it).render(violations, reportContext)
        }
        println("zally lint has been executed for ${input.inputSpec}")
    }

    private fun readSpec(fileLocation: String): String = File(fileLocation).readText()
}