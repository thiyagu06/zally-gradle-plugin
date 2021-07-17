package org.thiyagu.zally.rules

import org.junit.jupiter.api.Test
import org.thiyagu.zally.internal.ZallyViolationRules
import org.zalando.zally.rule.api.Severity
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HintRuleCheckerTest {

    @Test
    fun `should return true when result has more violation than allowed limit`() {
        val violationCount = mapOf(Severity.HINT to 5)
        val rule = ZallyViolationRules()
        rule.hint { max = 10 }
        assertFalse { HintRuleChecker.check(violationCount, rule) }
    }

    @Test
    fun `should return false when result has more violation than allowed limit`(){
        val violationCount = mapOf(Severity.HINT to 2)
        val rule = ZallyViolationRules()
        rule.hint { max = 1 }
        assertTrue { HintRuleChecker.check(violationCount, rule) }
    }

    @Test
    fun `should return false when result has no violation of type should`(){
        val violationCount = mapOf(Severity.MUST to 2)
        val rule = ZallyViolationRules()
        rule.hint { max = 1 }
        assertFalse { HintRuleChecker.check(violationCount, rule) }
    }
}
