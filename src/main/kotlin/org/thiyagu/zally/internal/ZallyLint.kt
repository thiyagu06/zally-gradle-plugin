package org.thiyagu.zally.internal

import org.gradle.api.Action
import java.io.File

open class ZallyLint  {

    var inputSpec: File? = null

    var ignoredRules: Array<String> = emptyArray()

    val reports = ZallyReports()

    fun reports(configure: Action<ZallyReports>) = configure.execute(reports)

}

