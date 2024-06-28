
FROM maven:3.9.6-eclipse-temurin-17 AS build
LABEL authors="Emmanuel"

# Set the working directory
COPY . ./usr/src/app
WORKDIR /usr/src/app

# Copy the pom.xml file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

## Set the working directory
WORKDIR /usr/src/app

# Copy the jar file from the build stage
COPY --from=build /usr/src/app/target/equicktrack-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

