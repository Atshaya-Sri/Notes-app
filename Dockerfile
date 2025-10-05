# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download project dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Package the application
RUN ./mvnw package -Dmaven.test.skip=true

# Set the command to run the application
CMD ["java", "-jar", "target/notes-app-backend-0.0.1-SNAPSHOT.jar"]