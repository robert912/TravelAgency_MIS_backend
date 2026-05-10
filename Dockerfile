FROM eclipse-temurin:21-jre-alpine
LABEL authors="Roberto Orellana"
ARG JAR_FILE=target/tourpackage-backend.jar
COPY ${JAR_FILE} tourpackage-backend.jar

ENTRYPOINT ["java", "-jar","/tourpackage-backend.jar"]