package org.thiyagu.zally.rules

import org.junit.jupiter.api.Test
import org.thiyagu.zally.internal.ZallyViolationRules
import org.zalando.zally.rule.api.Severity
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MustRuleCheckerTest {

    @Test
    fun `should return true when result has more violation than allowed limit`() {
        val violationCount = mapOf(Severity.MUST to 5)
        val rule = ZallyViolationRules()
        rule.must { max = 10 }
        assertFalse { MustRuleChecker.check(violationCount, rule) }
    }

    @Test
    fun `should return false when result has more violation than allowed limit`(){
        val violationCount = mapOf(Severity.MUST to 5)
        val rule = ZallyViolationRules()
        rule.must { max = 1 }
        assertTrue { MustRuleChecker.check(violationCount, rule) }
    }
}
