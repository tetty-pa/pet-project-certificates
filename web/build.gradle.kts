import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    id("com.google.protobuf") version "0.9.4"

    kotlin("jvm") version "1.9.0"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.allopen") version "1.9.10"
}

group = "com.epam.esm"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":persistence"))
    implementation(project(":service"))
    implementation(project(":nats"))

    api("io.jsonwebtoken:jjwt:0.9.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.1.2")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("cglib:cglib:3.3.0")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("io.nats:jnats:2.16.14")
    implementation("com.google.protobuf:protobuf-java:3.24.2")
    implementation("com.google.protobuf:protobuf-java-util:3.20.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
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

kotlin {
    jvmToolchain(17)
}

springBoot {
    mainClass.set("com.epam.esm.web.WebApplicationKt")
}
