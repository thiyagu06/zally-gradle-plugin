plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    id("com.github.ben-manes.versions") version "0.39.0"
    id("org.jetbrains.dokka") version "1.5.0"
    id("io.codearte.nexus-staging") version "0.30.0"
    jacoco
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
    jcenter()
    mavenLocal()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

val projectName = "zally-gradle-plugin"
val nexusUsername: String by project
val nexusPassword: String by project

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation(kotlin("test"))
    implementation("org.zalando:zally-core:2.0.0")
    implementation("org.zalando:zally-ruleset-zally:2.0.0")
    implementation("org.zalando:zally-ruleset-zalando:2.0.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.4")
    implementation("com.diogonunes:JColor:5.0.1")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.3")
}

gradlePlugin {
    plugins {
        register("zallyLint") {
            id = "io.github.thiyagu06"
            implementationClass = "org.thiyagu.zally.ZallyGradlePlugin"
        }
    }
}


tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "13"
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
            exclude("org/thiyagu/zally/rules/ZallyViolationRule.class")
            exclude()
        }
    )
    dependsOn(tasks.test) // tests are required to run before generating the report
}

(tasks.findByName("test") as Test).useJUnitPlatform()

tasks.jar {
    archiveBaseName.set(rootProject.name)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from("$buildDir/reports/javadoc")
}

publishing {

    publications {
        create<MavenPublication>("pluginMaven") {
            artifactId = projectName
            artifact(sourcesJar)
            artifact(javadocJar)
            setPomDetails(this)
        }
        afterEvaluate{
            named<MavenPublication>("zallyLintPluginMarkerMaven") {
                setPomDetails(this)
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
                username = nexusUsername
                password = nexusPassword
            }
        }
    }
}

signing {
    sign(publishing.publications)
}

fun setPomDetails(mavenPublication: MavenPublication) {
    mavenPublication.pom {
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

group = "io.github.thiyagu06"