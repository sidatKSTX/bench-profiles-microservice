# Stage 1: Build the application
FROM openjdk:21-slim AS build
WORKDIR /bench-profiles-service

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight runtime image
FROM openjdk:21-slim
WORKDIR /bench-profiles-service
COPY --from=build /bench-profiles-service/target/*.jar app.jar

# Use a minimal command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
