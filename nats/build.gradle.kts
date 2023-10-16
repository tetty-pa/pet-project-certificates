import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    java
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    id("com.google.protobuf") version "0.9.4"

    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.allopen") version "1.9.10"
}

group = "com.epam.esm"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("io.nats:jnats:2.16.14")
    implementation("com.google.protobuf:protobuf-java:3.24.2")
    implementation("com.google.protobuf:protobuf-java-util:3.20.1")
    implementation("io.projectreactor:reactor-core:3.5.10")
    implementation("io.grpc:grpc-protobuf:1.40.1")
    implementation("io.grpc:grpc-stub:1.40.1")
    implementation("io.grpc:grpc-netty:1.40.1")
    implementation("javax.annotation:javax.annotation-api:1.2")
    implementation("com.salesforce.servicelibs:reactor-grpc:1.2.4")
    implementation("com.salesforce.servicelibs:reactive-grpc-common:1.2.4")
    implementation("com.salesforce.servicelibs:reactor-grpc-stub:1.2.4")
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

tasks {
    bootJar {
        enabled = false
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.20.1"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.46.0"
        }
        create("reactor-grpc") {
            artifact = "com.salesforce.servicelibs:reactor-grpc:1.2.4"
        }
    }

    generateProtoTasks {
        all().configureEach {
            generateDescriptorSet = true
            descriptorSetOptions.includeImports = true
        }
        all().forEach {
            it.plugins {
                create("grpc")
                create("reactor-grpc")
            }
        }
    }
}
