FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /workspace
COPY pom.xml ./
COPY src ./src
RUN mvn --batch-mode --no-transfer-progress clean package -DskipTests

FROM eclipse-temurin:17-jre

RUN groupadd --system spring && useradd --system --gid spring spring
WORKDIR /app
COPY --from=build --chown=spring:spring /workspace/target/users-management-*.jar app.jar
USER spring:spring

# La JVM usa como heap hasta el 70 % de la memoria del contenedor
# (el plan gratuito de Render tiene 512 MB).
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
