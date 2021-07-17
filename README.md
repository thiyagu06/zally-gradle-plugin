# zally-gradle-plugin
[![CircleCI](https://circleci.com/gh/thiyagu06/zally-gradle-plugin/tree/main.svg?style=shield)](https://circleci.com/gh/thiyagu06/zally-gradle-plugin/tree/main)
[![Kotlin version badge](https://img.shields.io/badge/kotlin-1.5-blue.svg)](https://kotlinlang.org/docs/reference/whatsnew15.html)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)
[![APM](https://img.shields.io/apm/l/vim-mode)](LICENSE)
[![codecov](https://codecov.io/gh/thiyagu06/zally-gradle-plugin/branch/main/graph/badge.svg?token=18W2FTN9QL)](https://codecov.io/gh/thiyagu06/zally-gradle-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.thiyagu06/zally-gradle-plugin.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.thiyagu06/zally-gradle-plugin)


Runs [zally](https://github.com/zalando/zally) linter as gradle task and export the report in different format. 

https://medium.com/@thiyagu103/standardize-your-api-specification-using-gradle-tasks-and-linters-5c3d9e3ade99

## Advantages
 - no need to host and maintain zally server
 - export violation reports into different file format (json, html)
 - setup rules to define max number of violation allowed in the spec for each severity
 - Integrate zally as part of your build task for faster feedback
 - no external dependency like nodejs or go for running webUI and CLI

### How to run locally 
```
./gradlew clean build publishToMavenLocal
```

After that you are able to use/apply that plugin (even in this project) like this:
```
// settings.gradle.kts
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

// build.gradke.kts
plugins {
    id("io.github.thiyagu06") version "1.0.2-dev"
}

zallyLint {
    inputSpec = File("${projectDir}/docs/petstore-spec.yml")
    reports {
        json {
            enabled = true
            destination = File("${rootDir}/zally/violation.json")
        }
        rules {
            must {
               max = 10
            }
        }
    }
}

//execute task
./gradlew clean zallyLint

```

### How to release

Releasing to maven central is automated via circleci. 
Since this is public project, I disabled few environment variables in circleci to prevent access to nexus credentials.

1. Create a separate branch with a name `release-<release-version>`.
2. Update current version in `server/gradle.properties` from `-dev` to a final version.
3. Commit `server/gradle.properties` with the release version
4. Push the changes to remote origin.
   ```shell script
    git push origin `release-<release-version>`
    ```
5. create PR towards main branch and wait till circleci complete the `build` and `release` step.
6. Go to [https://app.circleci.com/](https://app.circleci.com/pipelines/github/thiyagu06/zally-gradle-plugin) and approve the job `releaseApproval`
5. Once the job is approved, circleci will publish the artifacts to maven central
6. Merge the PR into main branch
7. Checkout the latest main branch from remote origin in your local machine
   ```shell script
    git pull origin main
    ```
8. Create a tag
    ```shell script
    git tag v<release-version> -m "Version <release-version>"
    ```
9. Push the tag
   ```shell script
    git push origin <tag-name>
   ```

## License

MIT license with an exception. See [license file](LICENSE).
