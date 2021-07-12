package org.thiyagu.zally.reports

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.BOLD
import com.diogonunes.jcolor.Attribute.BRIGHT_RED_TEXT
import com.diogonunes.jcolor.Attribute.GREEN_TEXT
import com.diogonunes.jcolor.Attribute.RED_TEXT
import org.zalando.zally.core.Result
import java.nio.file.Files
import java.nio.file.Path

interface Reporter {
    fun write(violations: List<Result>, filePath: Path) {
        try {
            val reportData = render(violations)
            if (reportData != null) {
                filePath.parent?.let { Files.createDirectories(it) }
                Files.write(filePath, reportData.toByteArray())
                println(
                    colorize(
                        "The violation report can be found at ${filePath.toAbsolutePath()}",
                        RED_TEXT(),
                        BOLD()
                    )
                )
            }
        } catch (e: Exception) {
            println(
                colorize(
                    "could write violation report ${filePath.fileName} at ${filePath.toAbsolutePath()},error=${e.message}",
                    BOLD(), BRIGHT_RED_TEXT()
                )
            )
        }

    }

    fun render(violations: List<Result>): String?
}