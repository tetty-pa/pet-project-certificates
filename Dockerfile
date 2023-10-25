FROM openjdk:17-jdk-buster
ARG JAR_FILE=build/libs/Rest-api-basics-task-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","--add-opens=java.base/java.nio.charset=ALL-UNNAMED", "-jar", "app.jar"]
