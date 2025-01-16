# Use Amazon Corretto 21 for building the application
FROM amazoncorretto:21 as builder
WORKDIR /app

# Copy the project files
COPY . .

# Build the application
RUN ./gradlew clean bootJar --no-daemon

# Use Amazon Corretto 21 for running the application
FROM amazoncorretto:21
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/usersservice-0.0.1.jar app.jar

# Expose the port on which the application runs
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
