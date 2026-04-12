# Stage 1: Clone proto files from the infra repository
FROM alpine/git AS proto-stage
WORKDIR /proto-repo
RUN git clone https://github.com/xeubiart/infra.git .

# Stage 2: Build the Java app
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copy the Parent POM first to establish the project root
COPY pom.xml .

# 2. Copy all module POMs into their respective directories
# This allows Maven to resolve the parent/child relative paths
COPY xeubiart-app/pom.xml ./xeubiart-app/
COPY xeubiart-identity/pom.xml ./xeubiart-identity/
COPY xeubiart-core/pom.xml ./xeubiart-core/

# 3. Cache dependencies
# This layer will be reused unless you change a pom.xml
RUN mvn dependency:go-offline -B

# 4. Prepare Proto files
# We place them in the app module where they are needed for code generation
RUN mkdir -p xeubiart-app/src/main/resources/proto
COPY --from=proto-stage /proto-repo/proto/userService.proto ./xeubiart-app/src/main/resources/proto/

# 5. Copy the source code for all modules
COPY xeubiart-app/src ./xeubiart-app/src
COPY xeubiart-identity/src ./xeubiart-identity/src
COPY xeubiart-core/src ./xeubiart-core/src

# 6. Build the project
# This triggers the Maven Reactor to build modules in the correct order (core -> identity -> app)
RUN mvn clean install -DskipTests

# Stage 3: Runtime environment
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Add a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the fat JAR from the build stage
# Usually, the executable JAR is in the 'app' module's target folder
COPY --from=build /app/xeubiart-app/target/*.jar app.jar

# Standard Spring Boot port
EXPOSE 8080

# Optimization for faster startup and entropy
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]