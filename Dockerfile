# Start from a base image with Java 21
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Copy the source code
COPY src /app/src

# Build the application
RUN ./gradlew clean bootJar --no-daemon

# Expose port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "build/libs/online-food-delivery-order-service-0.0.1-SNAPSHOT.jar"]
