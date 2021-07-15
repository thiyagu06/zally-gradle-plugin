package org.thiyagu.zally.reports

import java.io.File

class ZallyReport(val reportType: ZallyReportType) {

    var enabled: Boolean = false

    var destination: File? = null

}

enum class ZallyReportType(val extension: String) {

    JSON("json"),
    HTML("html");

    fun defaultFile(directory: File): File = File(directory, "violations.$extension")
}
