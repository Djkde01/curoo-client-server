# Multi-stage build for optimized production image
FROM gradle:8.5-jdk17 AS build

# Set working directory
WORKDIR /app

# Copy gradle files first for better caching
COPY build.gradle settings.gradle ./
COPY gradle gradle

# Copy source code
COPY src src

# Build the application
RUN gradle build --no-daemon -x test

# Production image
FROM openjdk:17-jdk-slim

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create app directory and non-root user
RUN groupadd -r spring && useradd -r -g spring spring
WORKDIR /app
RUN chown spring:spring /app

# Copy the built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
