# Stage 1: Extract the layers from your pre-built fat JAR
FROM eclipse-temurin:25-jre-alpine AS builder
WORKDIR /build
COPY build/libs/app.jar app.jar
# Extract the jar using the modern 'tools' jarmode
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

# Stage 2: Create the final optimized image
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Security: Run as a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy extracted layers from the new 'extracted' directory
COPY --chown=spring:spring --from=builder /build/extracted/dependencies/ ./
COPY --chown=spring:spring --from=builder /build/extracted/spring-boot-loader/ ./
COPY --chown=spring:spring --from=builder /build/extracted/snapshot-dependencies/ ./
COPY --chown=spring:spring --from=builder /build/extracted/application/ ./

EXPOSE 8080
# Launch using Spring's specific loader class
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]