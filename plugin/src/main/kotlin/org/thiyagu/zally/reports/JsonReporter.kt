package org.thiyagu.zally.reports

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.zalando.zally.core.Result

class JsonReporter : Reporter {
    private val objectMapper = jacksonObjectMapper().writerWithDefaultPrettyPrinter()
    override fun render(violations: List<Result>): String = objectMapper.writeValueAsString(violations)
}
