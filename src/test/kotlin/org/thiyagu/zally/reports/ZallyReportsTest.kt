package org.thiyagu.zally.reports

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.thiyagu.zally.internal.ZallyReports

class ZallyReportsTest {

    @Test
    fun `configure html report`(){
        val zallyReports = ZallyReports()

        zallyReports.html {
            enabled = true
        }
        assertThat(zallyReports.enabled().size).isEqualTo(1)
        assertThat(zallyReports.enabled()[0].reportType.name).isEqualTo(ZallyReportType.HTML.name)
    }

    @Test
    fun `configure json report`(){
        val zallyReports = ZallyReports()

        zallyReports.json {
            enabled = true
        }
        assertThat(zallyReports.enabled().size).isEqualTo(1)
        assertThat(zallyReports.enabled()[0].reportType.name).isEqualTo(ZallyReportType.JSON.name)
    }
}
