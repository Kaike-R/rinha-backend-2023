FROM maven:3.8.4-openjdk-17-slim as build

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build ./build/target/*.jar ./application.jar

EXPOSE 8080

ENTRYPOINT java -jar application.jar