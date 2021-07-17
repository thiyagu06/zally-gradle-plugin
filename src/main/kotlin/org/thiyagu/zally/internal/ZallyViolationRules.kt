package org.thiyagu.zally.internal

import org.thiyagu.zally.rules.ZallyViolationRule

class ZallyViolationRules {

    internal val must = ZallyViolationRule()

    internal val should = ZallyViolationRule()

    internal val may = ZallyViolationRule()

    internal val hint = ZallyViolationRule()

    fun must(configure: ZallyViolationRule.() -> Unit) = must.configure()

    fun should(configure: ZallyViolationRule.() -> Unit) = should.configure()

    fun hint(configure: ZallyViolationRule.() -> Unit) = hint.configure()

    fun may(configure: ZallyViolationRule.() -> Unit) = may.configure()

}
