# Stage 1: Build the JAR
FROM openjdk:21-jdk-slim AS builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and config files
COPY gradlew ./gradlew
COPY gradle gradle
COPY build.gradle settings.gradle /app/
COPY src src

# Ensure gradlew is executable
RUN chmod +x gradlew

# Copy application source
# Build the application
RUN ./gradlew build -x test

# Stage 2: Run the JAR
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy only the built jar from the builder image
COPY --from=builder /app/build/libs/*.jar /app/

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]