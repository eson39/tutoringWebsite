FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew jar --no-daemon

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build build/libs/csa-api-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]