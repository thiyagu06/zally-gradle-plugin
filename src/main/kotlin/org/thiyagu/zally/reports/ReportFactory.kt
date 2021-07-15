package org.thiyagu.zally.reports

object ReportFactory {

    fun getReporter(format: ZallyReportType): Reporter {
        return when (format) {
            ZallyReportType.JSON -> JsonReporter()
            else -> throw IllegalArgumentException("the ${format.extension} is not supported")
        }
    }
}
