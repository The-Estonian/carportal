# Use a JDK image as the base image
FROM openjdk:21-jdk-slim as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and other necessary files
COPY gradle/ gradle/
COPY build.gradle settings.gradle /app/

# Copy the source code to the container
COPY src /app/src

# Run Gradle to build the JAR file (this assumes you have a gradle wrapper in your project)
RUN ./gradlew build -x test

# Now that we have the JAR, let's create a minimal image to run the app
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]