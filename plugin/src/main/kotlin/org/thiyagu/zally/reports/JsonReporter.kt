package org.thiyagu.zally.reports

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.zalando.zally.core.Result
import java.io.File

class JsonReporter : Reporter {
    private val fileName = "zally-violations.json"
    private val objectMapper = jacksonObjectMapper()
    override fun render(violations: List<Result>, reportContext: ReportContext) {
        try {
            val violationAsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(violations)
            val reportDir = File(reportContext.reportDir)
            if(reportDir.exists().not()) reportDir.mkdir()
            val jsonReport = File("${reportContext.reportDir}${File.separator}$fileName")
            jsonReport.createNewFile()
            jsonReport.writeText(violationAsJson)
            println(
                colorize(
                    "The violation report can be found at ${jsonReport.absolutePath}",
                    Attribute.YELLOW_TEXT(),
                    Attribute.BOLD()
                )
            )
        } catch (e: Exception) {
            println("could not generate the violation report in json format. message=${e.message}")
        }
    }
}