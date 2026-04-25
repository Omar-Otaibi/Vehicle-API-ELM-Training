# ─────────────────────────────────────────────
# layer 1 – BUILD
# ─────────────────────────────────────────────
FROM maven:3.9.12-eclipse-temurin-21 AS builder

# Set a working directory inside the build container
WORKDIR /app

##################
#COPY .mvn/ .mvn      To build from Maven Wrapper
#COPY mvnw pom.xml ./
#RUN sed -i 's/\r$//' mvnw
## Download dependencies offline using the wrapper
#RUN ./mvnw dependency:go-offline -B
##################

# Copy the Maven descriptor first so Docker can cache
# the dependency-download layer separately from the source.
# If pom.xml hasn't changed, this layer is reused.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the source code and build
#-B Batch mode for clean logs
COPY src ./src
RUN mvn clean package -DskipTests -B


# ─────────────────────────────────────────────
# layer 2 – RUNTIME
# ─────────────────────────────────────────────

# Light Alpine Linux to run the app
FROM eclipse-temurin:21-jre-alpine

# run as a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Copy only the fat JAR produced in Stage 1
COPY --from=builder /app/target/*.jar app.jar

#port
EXPOSE 8080

#Allow to inject JVM memory limits dynamically
#ENV JAVA_OPTS=""
# Start the application using shell execution to evaluate the JAVA_OPTS variable
#ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

ENTRYPOINT ["java", "-jar", "app.jar"]