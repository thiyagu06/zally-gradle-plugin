package org.thiyagu.zally.internal

import com.typesafe.config.ConfigFactory
import org.thiyagu.zally.rules.HintRuleChecker
import org.thiyagu.zally.rules.MayRuleChecker
import org.thiyagu.zally.rules.MustRuleChecker
import org.thiyagu.zally.rules.ShouldRuleChecker
import org.zalando.zally.core.CompositeRulesValidator
import org.zalando.zally.core.ContextRulesValidator
import org.zalando.zally.core.DefaultContextFactory
import org.zalando.zally.core.JsonRulesValidator
import org.zalando.zally.core.RulesManager
import org.zalando.zally.core.RulesPolicy

object ZallyFactory {

    private val rulesManager = RulesManager.fromClassLoader(ConfigFactory.load("rules-config.conf"))
    private val contextFactory = DefaultContextFactory()
    private val contextRulesValidator = ContextRulesValidator(rulesManager, contextFactory)
    val compositeRulesValidator =
        CompositeRulesValidator(contextRulesValidator, JsonRulesValidator(rulesManager))
    val rulesPolicy = RulesPolicy(emptyList())
    val violationRuleCheckers = listOf(MustRuleChecker, ShouldRuleChecker, MayRuleChecker, HintRuleChecker)
}
