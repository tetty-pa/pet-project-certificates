repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
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
