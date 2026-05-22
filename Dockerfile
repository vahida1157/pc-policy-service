# We only need the lightweight JRE, no JDK or Gradle needed here
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Security: Run as a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Simply copy the jar you ALREADY built on your host machine
COPY build/libs/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]