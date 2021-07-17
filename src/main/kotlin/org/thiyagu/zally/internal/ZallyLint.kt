package org.thiyagu.zally.internal

import org.gradle.api.Action
import java.io.File

open class ZallyLint  {

    var inputSpec: File? = null

    var ignoredRules: String? = null

    val reports = ZallyReports()

    val violationRules = ZallyViolationRules()

    fun reports(configure: Action<ZallyReports>) = configure.execute(reports)

    fun rules(configure: Action<ZallyViolationRules>) = configure.execute(violationRules)
}

