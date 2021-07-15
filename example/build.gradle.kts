version = "0.1"
group = "io.github.thiyagu06"

plugins {
    `application`
    id("io.github.thiyagu06") version "${replace_version_here}"
}

repositories {
    jcenter()
    mavenLocal()
    mavenCentral()
}

zallyLint {
    inputSpec = File("${rootDir}/spec/pet-store.yml")
    reports {
        json {
            enabled = true
            destination = File("${rootDir}/zally/violation.json")
        }
        html {
            enabled = true
            destination = File("${rootDir}/zally/violation.html")
        }
    }
}
