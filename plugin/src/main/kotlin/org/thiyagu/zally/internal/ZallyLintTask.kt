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
import org.zalando.zally.core.rulesConfig
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

open class ZallyLintTask : DefaultTask() {

    @TaskAction
    fun action() {
        val input = project.extensions.run {
            findByName("zallyLint") as ZallyExtension
        }
        if (input.inputSpec == null && input.inputSpecUrl == null) throw IllegalArgumentException("either inputspec or inputSpecUrl should be missing")
        val specContent = input.inputSpec ?: readApiFromUrl(input.inputSpecUrl!!)
        val rulesManager = RulesManager.fromClassLoader(rulesConfig)
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

    private fun readApiFromUrl(url: String): String {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()

        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        println("responseCode-->${response.statusCode()}")
        println("response-->${response.body()}")
        val contentType = response.headers().firstValue("Content-Type").orElse("")
        if (contentType.isEmpty()) {
            throw RuntimeException("unsupported media type $contentType")
        }
        return response.body()
    }

    companion object {
        private val MEDIA_TYPE_WHITELIST =
            listOf(
                // standard YAML mime-type plus variants
                "application/yaml",
                "application/x-yaml",
                "application/vnd.yaml",
                "text/yaml",
                "text/x-yaml",
                "text/vnd.yaml",

                // standard JSON mime-type plus variants
                "application/json",
                "application/javascript",
                "text/javascript",
                "text/x-javascript",
                "text/x-json",

                // github.com raw content pages issue text/plain content type for YAML
                "text/plain"
            )
    }

}