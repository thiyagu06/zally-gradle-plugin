package org.thiyagu.zally.reports

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.BOLD
import com.diogonunes.jcolor.Attribute.BRIGHT_GREEN_TEXT
import com.diogonunes.jcolor.Attribute.BRIGHT_MAGENTA_TEXT
import com.diogonunes.jcolor.Attribute.BRIGHT_RED_TEXT
import com.diogonunes.jcolor.Attribute.GREEN_TEXT
import com.diogonunes.jcolor.Attribute.RED_TEXT
import org.zalando.zally.core.Result
import org.zalando.zally.rule.api.Severity.HINT
import org.zalando.zally.rule.api.Severity.MAY
import org.zalando.zally.rule.api.Severity.MUST
import org.zalando.zally.rule.api.Severity.SHOULD
import java.nio.file.Path

class ConsoleReporter : Reporter {
    private val colorMapper = mapOf(
        MUST to BRIGHT_RED_TEXT(),
        SHOULD to RED_TEXT(),
        MAY to GREEN_TEXT(),
        HINT to BRIGHT_GREEN_TEXT()
    )

    override fun write(violations: List<Result>, filePath: Path) {
        render(violations)
    }

    override fun render(violations: List<Result>): String {
        if(violations.isEmpty()) {
            println(colorize("No violation identified in the spec. Great stuff!! \\U+1F389", GREEN_TEXT(), BOLD()))
        }else {
            val pad1 = "SEVERITY".length
            val pad2 = "COUNT".length
            val violationCountByType = violations.sortedBy { it.violationType }
                                                 .groupingBy { it.violationType }
                                                 .eachCount()
            println(colorize("-------- -----", BOLD(), GREEN_TEXT()))
            println(colorize("SEVERITY COUNT", BOLD(), BRIGHT_MAGENTA_TEXT()))
            println(colorize("-------- -----", BOLD(), GREEN_TEXT()))
            violationCountByType.forEach { (severity, count) ->
                print(colorize(severity.name.padEnd(pad1), colorMapper[severity]))
                println(colorize(count.toString().padStart(pad2), colorMapper[severity]))
            }
        }
        return ""
    }
}
