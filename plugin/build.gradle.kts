import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    id("maven-publish")
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
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

gradlePlugin {
    plugins {
        create("zallyLint") {
            id = "org.thiyagu.zally"
            implementationClass = "org.thiyagu.zally.ZallyGradlePlugin"
        }
    }
}

tasks{
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "15"
    }
}

(tasks.findByName("test") as Test).useJUnitPlatform()

group="org.thiyagu.zally"
version= "0.2.0-zally-maven-central"