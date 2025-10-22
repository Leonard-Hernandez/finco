FROM openjdk:17

WORKDIR /app

COPY . .

COPY mvnw /app/

RUN mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]