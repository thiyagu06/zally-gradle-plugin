package org.thiyagu.zally.rules

import org.junit.jupiter.api.Test
import org.thiyagu.zally.internal.ZallyViolationRules
import org.zalando.zally.rule.api.Severity
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ShouldRuleCheckerTest {

    @Test
    fun `should return true when result has more violation than allowed limit`() {
        val violationCount = mapOf(Severity.SHOULD to 5)
        val rule = ZallyViolationRules()
        rule.should { max = 10 }
        assertFalse { ShouldRuleChecker.check(violationCount, rule) }
    }

    @Test
    fun `should return false when result has more violation than allowed limit`(){
        val violationCount = mapOf(Severity.SHOULD to 2)
        val rule = ZallyViolationRules()
        rule.should { max = 1 }
        assertTrue { ShouldRuleChecker.check(violationCount, rule) }
    }

    @Test
    fun `should return false when result has no violation of type should`(){
        val violationCount = mapOf(Severity.MUST to 2)
        val rule = ZallyViolationRules()
        rule.should { max = 1 }
        assertFalse { ShouldRuleChecker.check(violationCount, rule) }
    }
}
