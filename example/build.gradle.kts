version = "0.1"
group = "io.github.thiyagu06"

plugins {
    `application`
    id("io.github.thiyagu06") version "1.2-dev"
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
        rules {
            should {
                max = 5
            }
            may {
                max = 40
            }
            hint {
                max = 10
            }
            must {
                max = 20
            }
        }
    }
}
