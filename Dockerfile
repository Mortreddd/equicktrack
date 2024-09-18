



# Stage 1: Build the application using Maven
FROM eclipse-temurin:22-jdk AS build
LABEL authors="Emmanuel"
# Set the working directory
WORKDIR /usr/src/app

# Copy the pom.xml file and download dependencies
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline
# Copy the source code and build the application
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application using a JDK
FROM eclipse-temurin:22-jre

COPY --from=build /usr/src/app/target/equicktrack-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8000

CMD ["java", "-jar", "app.jar"]


