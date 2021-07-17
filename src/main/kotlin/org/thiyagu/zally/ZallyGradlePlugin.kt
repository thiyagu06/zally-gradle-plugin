package org.thiyagu.zally

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.thiyagu.zally.internal.ZallyLint
import org.thiyagu.zally.internal.ZallyLintTask

class ZallyGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.run {
            create("zallyLint", ZallyLint::class.java)
        }
        with(project.tasks) {
            create("zallyLint", ZallyLintTask::class.java) {
                it.group = "zallyLinter"
                it.description = "API spec linter using zally"
            }
        }
    }
}
