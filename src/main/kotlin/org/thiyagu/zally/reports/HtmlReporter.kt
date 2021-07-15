package org.thiyagu.zally.reports

import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.stream.createHTML
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import org.zalando.zally.core.Result
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class HtmlReporter : Reporter {

    override fun render(violations: List<Result>): String? {
        return if(violations.isNotEmpty()) {
            javaClass.classLoader.getResource("violation-report-template.html")
                .openStream()
                .bufferedReader()
                .use { it.readText() }
                .replace("@@@summary@@@", renderSummary(violations))
                .replace("@@@findings@@@", renderFindings(violations))
                .replace("@@@date@@@", renderDate())
        } else null
    }

    private fun renderFindings(violations: List<Result>): String = createHTML().table {
        id = "findings"
        thead {
            tr {
                th { text("Rule ID") }
                th { text("Severity") }
                th { text("Title") }
                th { text("Description") }
                th { text("Line no") }
                th { text("Location") }
                th { text("More Info") }
            }
        }
        tbody {
            violations.sortedBy { it.violationType }.forEach {
                tr {
                    td { text(it.id) }
                    td { text(it.violationType.name) }
                    td { text(it.title) }
                    td { text(it.description) }
                    td { text(it.lines?.let { "${it.first..it.last}" } ?: "N/A") }
                    td { text(it.pointer.toString()) }
                    td {
                        a {
                            href = it.url.toString()
                            text(it.url.toString())
                        }
                    }
                }
            }
        }
    }

    private fun renderDate(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return OffsetDateTime.now(ZoneOffset.UTC).format(formatter) + " UTC"
    }


    private fun renderSummary(violations: List<Result>): String = createHTML().table {
        id = "summary"
        thead {
            tr {
                th { text("Severity") }
                th { text("Count") }
            }
        }
        tbody {
            val violationCountByType =
                violations.sortedBy { it.violationType }.groupingBy { it.violationType }.eachCount()
            violationCountByType.forEach { (severity, count) ->
                tr {
                    td { text("$severity") }
                    td { text("$count") }
                }
            }
        }
    }
}
