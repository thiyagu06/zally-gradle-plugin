package org.thiyagu.zally.internal

import com.typesafe.config.ConfigFactory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.zalando.zally.core.CompositeRulesValidator
import org.zalando.zally.core.ContextRulesValidator
import org.zalando.zally.core.DefaultContextFactory
import org.zalando.zally.core.JsonRulesValidator
import org.zalando.zally.core.RulesManager
import org.zalando.zally.core.RulesPolicy

open class ZallyLintTask : DefaultTask() {

    @TaskAction
    fun action() {
        val input = project.extensions.run {
            findByName("zallyLint") as ZallyExtension
        }
        val specContent = input.inputSpec ?: throw IllegalArgumentException("either input spec should be provided")
        val rulesManager = RulesManager.fromClassLoader(ConfigFactory.load("rules-config.conf"))
        val contextFactory = DefaultContextFactory()
        val contextRulesValidator = ContextRulesValidator(rulesManager, contextFactory)
        val compositeRulesValidator = CompositeRulesValidator(contextRulesValidator, JsonRulesValidator(rulesManager))
        val violation = compositeRulesValidator.validate(specContent, RulesPolicy(input.ignoredRules.toList()))
        println(violation.forEach {
            "${it.description}-${it.pointer}-${it.lines}-${it.title}-${it.url}-${it.violationType}"
        })
        val summary = violation.groupBy { it.violationType }
        summary.forEach { (t, u) ->
            println("$t-$u")
        }
    }

}