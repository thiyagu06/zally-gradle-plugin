package org.thiyagu.zally.internal

import org.thiyagu.zally.reports.ZallyReport
import org.thiyagu.zally.reports.ZallyReportType

class ZallyReports {

    private val json = ZallyReport(ZallyReportType.JSON)

    private val html = ZallyReport(ZallyReportType.HTML)

    internal fun enabled(): List<ZallyReport> = listOf(json, html).filter { it.enabled }

    fun json(configure: ZallyReport.() -> Unit) = json.configure()

    fun html(configure: ZallyReport.() -> Unit) = html.configure()
}
