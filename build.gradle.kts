import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val excludesPaths: Iterable<String> = listOf()

plugins {
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
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
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
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
