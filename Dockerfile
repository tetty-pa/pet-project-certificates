FROM openjdk:17-jdk-buster
ARG JAR_FILE=web/build/libs/web-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","--add-opens=java.base/java.nio.charset=ALL-UNNAMED", "-jar", "app.jar"]
