FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} weebook-api-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "weebook-api-app.jar"]