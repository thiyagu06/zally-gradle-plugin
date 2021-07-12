package org.thiyagu.zally.reports

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute
import mu.KLogging
import org.zalando.zally.core.Result
import org.zalando.zally.rule.api.Severity.MUST
import org.zalando.zally.rule.api.Severity.MAY
import org.zalando.zally.rule.api.Severity.SHOULD
import org.zalando.zally.rule.api.Severity.HINT

class ConsoleReporter : Reporter {
    private val logger = KLogging().logger
    private val colorMapper = mapOf(
        MUST to Attribute.BRIGHT_RED_TEXT(),
        SHOULD to Attribute.RED_TEXT(),
        MAY to Attribute.GREEN_TEXT(),
        HINT to Attribute.BRIGHT_GREEN_TEXT()
    )

    override fun render(violations: List<Result>, reportContext: ReportContext) {
        if (violations.isEmpty()) {
            logger.debug("provided spec is valid!!")
            println(colorize("Great stuff!! \\U+1F389 No violation identified in the spec", Attribute.GREEN_TEXT()))
            return
        }
        val pad1 = "SEVERITY".length
        val pad2 = "COUNT".length
        val violationCountByType = violations.sortedBy { it.violationType }.groupingBy { it.violationType }.eachCount()
        println(colorize("-------- -----", Attribute.BOLD(), Attribute.GREEN_TEXT()))
        println(colorize("SEVERITY COUNT", Attribute.BOLD(), Attribute.BRIGHT_MAGENTA_TEXT()))
        println(colorize("-------- -----", Attribute.BOLD(), Attribute.GREEN_TEXT()))
        violationCountByType.forEach { (severity, count) ->
            print(colorize(severity.name.padEnd(pad1), colorMapper[severity]))
            println(colorize(count.toString().padStart(pad2), colorMapper[severity]))
        }
    }
}