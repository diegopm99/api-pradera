FROM ubuntu:lastest AS build
RUN apt-get update
RUN apt-get install openjdk-11-jdk -y
COPY . .
RUN ./mvnw spring-boot:run

FROM openjdk:11-jdk-slim
EXPOSE 8080
COPY --from=build /target/PraderaBack-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]