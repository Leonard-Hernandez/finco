FROM amazoncorretto:17-alpine3.20-jdk AS builder

WORKDIR /app/finco

COPY ./.mvn ./.mvn
COPY ./mvnw .
COPY ./pom.xml .
RUN chmod +x mvnw && sed -i 's/\r$//' mvnw
RUN ./mvnw package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8086

FROM sourcemation/jre-17

WORKDIR /app

COPY --from=builder /app/finco/target/finco-0.0.1-SNAPSHOT.jar .

ENTRYPOINT [ "java", "-jar", "finco-0.0.1-SNAPSHOT.jar" ]