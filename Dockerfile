FROM maven:3-eclipse-temurin-22 AS builder

WORKDIR /metrics
COPY . .
RUN mvn clean package

FROM eclipse-temurin:21-jre

ENV TZ=Europe/Berlin

WORKDIR /metrics
COPY --from=builder /metrics/rest/target/rest-*-SNAPSHOT.jar rest.jar

EXPOSE 8080
ENTRYPOINT java -jar /metrics/rest.jar
