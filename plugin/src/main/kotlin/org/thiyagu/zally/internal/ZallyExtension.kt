package org.thiyagu.zally.internal

import org.thiyagu.zally.reports.ReportFormat
import org.thiyagu.zally.reports.ReportFormat.CONSOLE

open class ZallyExtension {

    var inputSpec: String? = null

    var ignoredRules: Array<String> = emptyArray()

    var reportFormats: Array<ReportFormat> = arrayOf(CONSOLE)

    var reportDir: String? = null

}