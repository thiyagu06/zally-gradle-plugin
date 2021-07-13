
plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    id("maven-publish")
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    id("com.github.ben-manes.versions") version "0.20.0"
    id("org.jetbrains.dokka") version "1.5.0"
    jacoco
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
    jcenter()
    mavenLocal()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation(kotlin("test"))
    implementation("org.springframework:spring-context:5.3.8")
    implementation("org.zalando:zally-core:2.0.0")
    implementation("org.zalando:zally-ruleset-zally:2.0.0")
    implementation("org.zalando:zally-ruleset-zalando:2.0.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.4")
    implementation("com.diogonunes:JColor:5.0.1")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

gradlePlugin {
    plugins {
        create("zallyLint") {
            id = "org.thiyagu.zally"
            implementationClass = "org.thiyagu.zally.ZallyGradlePlugin"
        }
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "15"
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacoco"))
        xml.outputLocation.set(file("$buildDir/jacoco/jacocoTestReport.xml"))
    }
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude("org/thiyagu/zally/internal/ZallyLint.class")
            exclude("org/thiyagu/zally/internal/ZallyFactory.class")
            exclude("org/thiyagu/zally/reports/ZallyReport.class")
            exclude("org/thiyagu/zally/reports/ZallyReportType.class")
            exclude()
        }
    )
    dependsOn(tasks.test) // tests are required to run before generating the report
}

(tasks.findByName("test") as Test).useJUnitPlatform()

tasks.jar {
    archiveBaseName.set(rootProject.name)
}

tasks.register("javadocJar", Jar::class) {
    dependsOn(tasks["dokkaJavadoc"])
    archiveClassifier.set("javadoc")
    from(tasks["dokkaJavadoc"])
}

tasks.register("sourcesJar", Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    add("archives", tasks["javadocJar"])
    add("archives", tasks["sourcesJar"])
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                description.set("OpenAPI linter service")
                url.set("https://github.com/thiyagu06/zally-gradle-plugin")
                name.set("Gradle plugin for zally Linter")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        name.set("Thiyagu GK")
                        email.set("thiyagu103@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/thiyagu06/zally-gradle-plugin.git")
                    developerConnection.set("scm:git:ssh://github.com:thiyagu06/zally-gradle-plugin.git")
                    url.set("https://github.com/thiyagu06/zally-gradle-plugin/tree/main")
                }
            }
        }
    }

    repositories {
        maven {
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                // defined in travis project settings or in $HOME/.gradle/gradle.properties
                username = System.getenv("OSSRH_JIRA_USERNAME")
                password = System.getenv("OSSRH_JIRA_PASSWORD")
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

group = "org.thiyagu.zally"