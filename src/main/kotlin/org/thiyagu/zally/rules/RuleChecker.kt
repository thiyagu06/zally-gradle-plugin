package org.thiyagu.zally.rules

import org.thiyagu.zally.internal.ZallyViolationRules
import org.zalando.zally.rule.api.Severity


interface RuleChecker {

    fun check(violationByCount: Map<Severity, Int>, rules: ZallyViolationRules): Boolean
}
