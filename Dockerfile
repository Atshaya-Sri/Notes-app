# Use an official Maven image with JDK 17
FROM maven:3.9.8-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only pom.xml first (to leverage Docker cache)
COPY pom.xml .

# Pre-download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the app (skip tests for faster build)
RUN mvn clean package -Dmaven.test.skip=true

# --- Runtime image ---
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
