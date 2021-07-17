package org.thiyagu.zally.rules

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.BOLD
import com.diogonunes.jcolor.Attribute.RED_TEXT
import org.thiyagu.zally.internal.ZallyViolationRules
import org.zalando.zally.rule.api.Severity
import org.zalando.zally.rule.api.Severity.MAY

object MayRuleChecker : RuleChecker {

    override fun check(violationByCount: Map<Severity, Int>, rules: ZallyViolationRules): Boolean {
        val mayRule = rules.may
        return if (violationByCount.getOrDefault(MAY, 0) >= mayRule.max) {
            println(
                colorize(
                    """
                     spec has ${violationByCount[MAY]} has violations 
                     which is larger than allowed ${mayRule.max} value   
                    """.trimIndent(),
                    RED_TEXT(),
                    BOLD()
                )
            )
            true
        } else false
    }
}
