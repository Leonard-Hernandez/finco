FROM amazoncorretto:25-alpine AS builder

WORKDIR /app/finco

COPY gradle ./gradle
COPY gradlew .
COPY build.gradle settings.gradle ./
RUN ./gradlew dependencies --no-daemon || true

COPY ./src ./src

RUN ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:25-jre-alpine

RUN addgroup -S finco && adduser -S -G finco -H -s /sbin/nologin finco

USER finco

WORKDIR /app

COPY --from=builder --chown=finco:finco /app/finco/build/libs/finco-0.0.1-SNAPSHOT.jar .

EXPOSE 8086

ENTRYPOINT [ "java", "-jar", "finco-0.0.1-SNAPSHOT.jar" ]
