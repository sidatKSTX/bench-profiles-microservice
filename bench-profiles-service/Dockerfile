# Stage 1: Build the application
# FROM openjdk:21-slim AS build  # Original Debian-based image
FROM eclipse-temurin:21.0.7_6-jdk-alpine-3.21 AS build
WORKDIR /bench-profiles-service

# Install Maven (Alpine uses apk instead of apt)
RUN apk add --no-cache maven

COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight runtime image
# FROM openjdk:21-slim  # Original Debian-based image
FROM eclipse-temurin:21.0.7_6-jre-alpine-3.21
WORKDIR /bench-profiles-service
COPY --from=build /bench-profiles-service/target/*.jar bench-profiles-service.jar

# Use a minimal command to run the application
ENTRYPOINT ["java", "-jar", "bench-profiles-service.jar"]
