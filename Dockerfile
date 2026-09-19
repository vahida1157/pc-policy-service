# Stage 1: Extract the layers using Spring's official tool
FROM eclipse-temurin:25-jre-alpine AS builder
WORKDIR /build
COPY build/libs/app.jar app.jar
# Added --launcher so the JarLauncher class is not thrown away!
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted --launcher

# Stage 2: Create the final optimized image
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Security: Run as a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy extracted layers in order (libraries first, your code last)
COPY --chown=spring:spring --from=builder /build/extracted/dependencies/ ./
COPY --chown=spring:spring --from=builder /build/extracted/spring-boot-loader/ ./
COPY --chown=spring:spring --from=builder /build/extracted/snapshot-dependencies/ ./
COPY --chown=spring:spring --from=builder /build/extracted/application/ ./

EXPOSE 8080
# Run the layered app successfully!
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]