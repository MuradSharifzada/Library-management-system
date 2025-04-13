
FROM openjdk:17-jdk-slim


WORKDIR /app

# Copy the built JAR from your Gradle build
COPY build/libs/LibraryManagementSystem-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8081

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
