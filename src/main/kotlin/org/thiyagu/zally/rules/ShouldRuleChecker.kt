package org.thiyagu.zally.rules

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.BOLD
import com.diogonunes.jcolor.Attribute.RED_TEXT
import org.thiyagu.zally.internal.ZallyViolationRules
import org.zalando.zally.rule.api.Severity
import org.zalando.zally.rule.api.Severity.SHOULD

object ShouldRuleChecker : RuleChecker {

    override fun check(violationByCount: Map<Severity, Int>, rules: ZallyViolationRules): Boolean {
        val shouldRule = rules.should
        return if (violationByCount.getOrDefault(SHOULD, 0) > shouldRule.max) {
            println(
                colorize("spec has ${violationByCount[SHOULD]} $SHOULD violations." +
                    "but only ${shouldRule.max} violations allowed",
                    RED_TEXT(),
                    BOLD()
                )
            )
            true
        } else false
    }
}
