FROM maven:3-eclipse-temurin-21 AS builder

WORKDIR /metrics
COPY . .
RUN mvn clean package
RUN rm /metrics/rest/target/*-javadoc.jar && rm /metrics/rest/target/*-sources.jar

FROM eclipse-temurin:21-jre

ENV TZ=Europe/Berlin

WORKDIR /metrics
COPY --from=builder /metrics/rest/target/rest-*.jar rest.jar

EXPOSE 8080
ENTRYPOINT java -jar /metrics/rest.jar
