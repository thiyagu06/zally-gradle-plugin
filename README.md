# zally-gradle-plugin

With more and more adoption towards micro services, software products being more and more just a bunch of micro-services and third-party APIs mashed together, it gets more crucial for us to get their structure in order.With API as product as get traction among the industry standardization of the API in the organization also become inevitable.

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
    }
}

//execute task
./gradlew clean zallyLint

```

### TODO

- [x] write unit test

- [x] export violation report as html

- [x] add ktlint and detekt to improve code quality

- [x] add jacoco for test coverage

- [x] publish to maven central

- [ ] allow plugin to define threshold for SHOULD and MUST severity violations. If threshold breaches, build should fail

- [x] improve exception handling wherever possible (e.g file operations)

- [ ] release script

- [x] configure circleci

- [x] create sample project to showcase the plugin

- [ ]  enable dependabot
