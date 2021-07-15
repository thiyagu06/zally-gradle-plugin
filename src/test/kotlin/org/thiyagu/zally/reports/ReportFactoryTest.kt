package org.thiyagu.zally.reports

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.thiyagu.zally.reports.ZallyReportType.HTML
import org.thiyagu.zally.reports.ZallyReportType.JSON

class ReportFactoryTest {

    @Test
    fun `should return the instance based on the report type`() {
        assertThat(ReportFactory.getReporter(JSON)).isInstanceOf(JsonReporter::class.java)
        assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy { ReportFactory.getReporter(HTML) }
    }
}
