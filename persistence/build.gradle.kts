import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.allopen") version "1.9.10"
    kotlin("plugin.noarg") version "1.9.10"
}

group = "com.epam.esm"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api("io.jsonwebtoken:jjwt:0.9.0")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.1.2")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    compileOnly("org.springframework.boot:spring-boot-starter-hateoas")

    testCompileOnly("org.springframework.boot:spring-boot-starter-hateoas")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.9"
}

tasks {
    bootJar {
        enabled = false
    }
}
allOpen {
    annotation("org.springframework.data.mongodb.core.mapping.Document")
}
