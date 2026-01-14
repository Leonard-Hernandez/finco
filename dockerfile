FROM openjdk:17.0.2

WORKDIR /app/finco

COPY ./.mvn ./.mvn
COPY ./mvnw .
COPY ./pom.xml .
RUN chmod +x mvnw && sed -i 's/\r$//' mvnw
RUN ./mvnw package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8086

ENTRYPOINT [ "java", "-jar", "./target/finco-0.0.1-SNAPSHOT.jar" ]