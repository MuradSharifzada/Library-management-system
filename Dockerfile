
FROM openjdk:17-jdk-slim


WORKDIR /app


COPY build/libs/LibraryManagementSystem-0.0.1-SNAPSHOT.jar app.jar


ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
