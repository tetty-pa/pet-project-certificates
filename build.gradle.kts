import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.gitlab.arturbosch.detekt") version ("1.23.1")
    java
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    id("com.google.protobuf") version "0.9.4"

    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.allopen") version "1.9.10"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

group = "com.epam.esm"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":gift-certificate-subdomain"))
    implementation(project(":tag-subdomain"))
    implementation(project(":user-subdomain"))
    implementation(project(":order-subdomain"))
    implementation(project(":shared-subdomain"))
    implementation(project(":nats"))

    api("io.jsonwebtoken:jjwt:0.9.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:3.1.3")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:3.1.4")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.4")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("cglib:cglib:3.3.0")
    implementation("io.nats:jnats:2.16.14")
    implementation("com.google.protobuf:protobuf-java:3.24.2")
    implementation("com.google.protobuf:protobuf-java-util:3.20.1")
    implementation("io.grpc:grpc-stub:1.58.0")
    implementation("io.grpc:grpc-protobuf:1.58.0")
    implementation("io.grpc:grpc-netty:1.58.0")
    implementation("com.salesforce.servicelibs:reactor-grpc:1.2.4")
    implementation("com.salesforce.servicelibs:reactive-grpc-common:1.2.4")
    implementation("com.salesforce.servicelibs:reactor-grpc-stub:1.2.4")
    implementation ("io.netty:netty-resolver-dns-native-macos")
    implementation("org.springframework.kafka:spring-kafka:3.0.11")
    implementation("io.projectreactor.kafka:reactor-kafka:1.3.21")

    testImplementation("io.projectreactor:reactor-test:3.5.10")
    testImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
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

kotlin {
    jvmToolchain(17)
}

springBoot {
    mainClass.set("com.epam.esm.web.WebApplicationKt")
}


group = "com.epam.esm"
version = "1.0-SNAPSHOT"

subprojects {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://repo.maven.apache.org/maven2/")
        }
    }
}
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("1.9.0")
        }
    }
}
