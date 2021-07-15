package org.thiyagu.zally.reports

object ReportFactory {

    fun getReporter(format: ZallyReportType): Reporter {
        return when (format) {
            ZallyReportType.JSON -> JsonReporter()
            ZallyReportType.HTML -> HtmlReporter()
        }
    }
}
