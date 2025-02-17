# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the root pom.xml and all module pom.xml files
COPY pom.xml .
COPY app/pom.xml app/pom.xml
COPY domain/pom.xml domain/pom.xml
COPY music-connectors/pom.xml music-connectors/pom.xml

# Download dependencies
RUN mvn dependency:go-offline

# Copy the entire project and build the application
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jdk

# Set the working directory for the application
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/app/target/app-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]