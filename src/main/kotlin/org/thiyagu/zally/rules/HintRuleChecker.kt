package org.thiyagu.zally.rules

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.BOLD
import com.diogonunes.jcolor.Attribute.RED_TEXT
import org.thiyagu.zally.internal.ZallyViolationRules
import org.zalando.zally.rule.api.Severity
import org.zalando.zally.rule.api.Severity.HINT

object HintRuleChecker : RuleChecker {

    override fun check(violationByCount: Map<Severity, Int>, rules: ZallyViolationRules): Boolean {
        val hintRule = rules.hint
        return if (violationByCount.getOrDefault(HINT, 0) >= hintRule.max) {
            println(
                colorize("spec has ${violationByCount[HINT]} $HINT violations." +
                    "but only ${hintRule.max} violations allowed",
                    RED_TEXT(),
                    BOLD()
                )
            )
            true
        } else false
    }
}
