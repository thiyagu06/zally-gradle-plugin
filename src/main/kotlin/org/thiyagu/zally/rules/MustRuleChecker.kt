package org.thiyagu.zally.rules

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.BOLD
import com.diogonunes.jcolor.Attribute.RED_TEXT
import org.thiyagu.zally.internal.ZallyViolationRules
import org.zalando.zally.rule.api.Severity
import org.zalando.zally.rule.api.Severity.MUST

object MustRuleChecker : RuleChecker {

    override fun check(violationByCount: Map<Severity, Int>, rules: ZallyViolationRules): Boolean {
        val mustRule = rules.must
        return if (violationByCount.getOrDefault(MUST, 0) >= mustRule.max) {
            println(
                colorize(
                    """
                      spec has ${violationByCount[MUST]} has violations which 
                      is larger than allowed ${mustRule.max} value  
                    """.trimIndent(),
                    RED_TEXT(),
                    BOLD()
                )
            )
            true
        } else false
    }
}
