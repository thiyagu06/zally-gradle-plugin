# zally-gradle-plugin

WIP

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
    id("org.thiyagu.zally") version "0.0.2-SNAPSHOT"
}

zallyLint {
    inputSpec = "${projectDir}/spec.yml"
    reportDir = "$rootDir/zally"
    reportFormats = arrayOf(JSON)
}

//execute task
./gradlew clean zallyLint

```

TODO

[ ] write unit test

[ ] export violation report as html

[ ] add ktlint and detekt to improve code quality

[ ] add jacoco for test coverage

[ ] publish to maven central

[ ] allow plugin to define threshold for SHOULD and MUST severity violations. If threshold breaches, build should fail

[ ] improve exception handling wherever possible (e.g file operations)

[ ] release script