package org.thiyagu.zally.rules

import org.junit.jupiter.api.Test
import org.thiyagu.zally.internal.ZallyViolationRules
import org.zalando.zally.rule.api.Severity
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MayRuleCheckerTest {

    @Test
    fun `should return true when result has more violation than allowed limit`() {
        val violationCount = mapOf(Severity.MAY to 5)
        val rule = ZallyViolationRules()
        rule.may { max = 10 }
        assertFalse { MayRuleChecker.check(violationCount, rule) }
    }

    @Test
    fun `should return false when result has more violation than allowed limit`(){
        val violationCount = mapOf(Severity.MAY to 2)
        val rule = ZallyViolationRules()
        rule.may { max = 1 }
        assertTrue { MayRuleChecker.check(violationCount, rule) }
    }

    @Test
    fun `should return false when result has no violation of type should`(){
        val violationCount = mapOf(Severity.MUST to 2)
        val rule = ZallyViolationRules()
        rule.may { max = 1 }
        assertFalse { MayRuleChecker.check(violationCount, rule) }
    }
}
