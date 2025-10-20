# Dockerfile
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
ARG JAR_FILE=target/task-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
