FROM maven:3.9.9-eclipse-temurin AS builder
WORKDIR /app
COPY . /app
RUN --mount=type=cache,target=/root/.m2 mvn clean package -pl zephyr-common/common-boot -am -DskipTests -B

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
COPY --from=builder /app/zephyr-common/common-boot/target/common-boot-*.jar app.jar
EXPOSE 8072 8073
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8072/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]