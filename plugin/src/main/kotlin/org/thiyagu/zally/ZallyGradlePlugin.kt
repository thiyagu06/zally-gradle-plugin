package org.thiyagu.zally

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.thiyagu.zally.internal.ZallyExtension
import org.thiyagu.zally.internal.ZallyLintTask

class ZallyGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.run {
            create("zallyLint", ZallyExtension::class.java)
        }
        with(project.tasks) {
            create("zallyLint", ZallyLintTask::class.java) {
                it.group = "zallyLinter"
                it.description = "API spec linter using zally"
            }
        }
    }
}
