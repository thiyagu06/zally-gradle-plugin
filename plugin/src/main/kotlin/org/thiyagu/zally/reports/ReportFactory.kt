package org.thiyagu.zally.reports

object ReportFactory {

    fun getReporter(format: ReportFormat): Reporter {
        return when (format) {
            ReportFormat.JSON -> JsonReporter()
            ReportFormat.CONSOLE -> ConsoleReporter()
        }
    }
}