FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} weebook-api-app.jar
ENV SRPING_PROFILES_ACTIVE=prod
ENV SRPING_PROFILES_DATASOURCE_URL=jdbc:postgresql://db.hxoergheeitlgfbitgsk.supabase.co:5432/postgres
ENV SRPING_PROFILES_DATASOURCE_USERNAME=postgres
ENV SRPING_PROFILES_DATASOURCE_PASSWORD=00B9RiBS0yIXftVk
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "weebook-api-app.jar"]