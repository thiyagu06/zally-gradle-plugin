package org.thiyagu.zally.rules

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.thiyagu.zally.internal.ZallyViolationRules

class ZallyViolationRulesTest {

    @Test
    fun `should set given value in the max property`(){
        val rules = ZallyViolationRules()
        rules.should { max = 5 }
        rules.must { max = 10 }
        rules.hint { max = 20 }
        rules.may { max = 4 }

        assertThat(rules.should.max).isEqualTo(5)
        assertThat(rules.must.max).isEqualTo(10)
        assertThat(rules.hint.max).isEqualTo(20)
        assertThat(rules.may.max).isEqualTo(4)
    }

    @Test
    fun `default value should of max property should be MAX_VALUE when not set`(){
        val rules = ZallyViolationRules()
        assertThat(rules.may.max).isEqualTo(Int.MAX_VALUE)
        assertThat(rules.must.max).isEqualTo(Int.MAX_VALUE)
        assertThat(rules.hint.max).isEqualTo(Int.MAX_VALUE)
        assertThat(rules.may.max).isEqualTo(Int.MAX_VALUE)
    }
}
