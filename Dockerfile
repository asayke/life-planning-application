FROM eclipse-temurin:17-jdk-jammy

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]