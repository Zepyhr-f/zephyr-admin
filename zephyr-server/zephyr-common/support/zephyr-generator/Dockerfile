FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . /app
RUN --mount=type=cache,target=/root/.m2 mvn clean package -pl zephyr-common/support/zephyr-generator -am -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY --from=builder /app/zephyr-common/support/zephyr-generator/target/*.jar app.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "app.jar"]
