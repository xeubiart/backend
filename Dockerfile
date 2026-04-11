# Stage 1: Clone proto files
FROM alpine/git AS proto-stage
WORKDIR /proto-repo
RUN git clone https://github.com/xeubiart/infra.git .

# Stage 2: Build the Java app
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copy the Parent POM and Module POMs to cache dependencies
COPY pom.xml .
COPY xeubiart-app/pom.xml ./xeubiart-app/
COPY xeubiart-identity/pom.xml ./xeubiart-identity/

RUN mvn dependency:go-offline -B || true

# 2. Create the proto directory inside the SPECIFIC module
RUN mkdir -p xeubiart-app/src/main/resources/proto

# 3. Copy the proto file (This is the only one you need!)
COPY --from=proto-stage /proto-repo/proto/userService.proto ./xeubiart-app/src/main/resources/proto/

# 4. Copy the actual source folders for each module
COPY xeubiart-app/src ./xeubiart-app/src
COPY xeubiart-identity/src ./xeubiart-identity/src

# 5. Build
RUN mvn package -DskipTests

# Stage 3: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Grabs the JAR from the app module's target folder
COPY --from=build /app/xeubiart-app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]