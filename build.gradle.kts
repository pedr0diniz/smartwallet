import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

val excludesPaths: Iterable<String> = listOf(
    "br/com/zinid/smartwallet/application/config/**"
)

plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    jacoco
}

group = "br.com.zinid"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

sourceSets {
    create("componentTest") {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }

    create("archTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

ext {
    set("excludePackages", excludesPaths)
}

val componentTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val archTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

configurations["componentTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

configurations["archTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

val componentTestTask = tasks.create("componentTest", Test::class) {
    description = "Runs the component tests."
    group = "verification"

    testClassesDirs = sourceSets["componentTest"].output.classesDirs
    classpath = sourceSets["componentTest"].runtimeClasspath

    useJUnitPlatform()
    shouldRunAfter("test")
}

val archTest = tasks.create("archTest", Test::class) {
    description = "Runs the architecture tests."
    group = "verification"

    testClassesDirs = sourceSets["archTest"].output.classesDirs
    classpath = sourceSets["archTest"].runtimeClasspath

    useJUnitPlatform()
    shouldRunAfter("componentTest")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.0")

    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.mockk:mockk:1.12.0")

    archTestImplementation("org.junit.jupiter:junit-jupiter")
    archTestImplementation("com.tngtech.archunit:archunit-junit5-api:1.0.1")
    archTestImplementation("com.tngtech.archunit:archunit-junit5-engine:1.0.1")
}

fun loadEnv(environment: MutableMap<String, Any>, file: File) {
    if (!file.exists()) throw IllegalArgumentException("failed to load envs from file, ${file.name} not found")

    file.readLines().forEach { line ->
        if (line.isBlank() || line.startsWith("#")) return@forEach
        line.split("=", limit = 2)
            .takeIf { it.size == 2 && !it[0].isBlank() }
            ?.run { Pair(this[0].trim(), this[1].trim()) }
            ?.run {
                environment[this.first] = this.second
            }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootRun> {
    loadEnv(environment, file("variables.local.env"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test, componentTestTask)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(excludesPaths)
        }
    )
    executionData(
        file("${project.buildDir}/jacoco/test.exec"),
        file("${project.buildDir}/jacoco/componentTest.exec")
    )
}

tasks.jacocoTestCoverageVerification {
    dependsOn(
        tasks.test,
        componentTestTask,
        tasks.jacocoTestReport
    )
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(excludesPaths)
        }
    )
    violationRules {
        rule {
            limit {
                minimum = 0.6.toBigDecimal()
                counter = "LINE"
            }
        }
        rule {
            includes = listOf("br.com.zinid.*")
            limit {
                minimum = 0.6.toBigDecimal()
                counter = "BRANCH"
            }
        }
    }
    executionData(
        file("${project.buildDir}/jacoco/test.exec"),
        file("${project.buildDir}/jacoco/componentTest.exec")
    )
}

tasks.test {
    finalizedBy("jacocoTestReport", "jacocoTestCoverageVerification", "componentTest", "archTest")
}

detekt {
    input = files("src")
    config = files("detekt-config.yml")
    reports {
        html {
            enabled = true
            destination = file("build/reports/detekt.html")
        }
    }
}
