FROM maven:3.8.3-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dskiptest

FROM openjdk:17.0.1-jdk-slim
ARG JAR_FILE=/home/app/target/*.jar
COPY --from=build ${JAR_FILE} weebook-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "weebook-api.jar"]