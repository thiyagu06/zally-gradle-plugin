package org.thiyagu.zally.reports

import org.zalando.zally.core.Result

interface Reporter {
    fun render(violations: List<Result>, reportContext: ReportContext)
}

data class ReportContext(val reportDir:String)

enum class ReportFormat {
    JSON,
    CONSOLE
}