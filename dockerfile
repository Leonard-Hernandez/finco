FROM openjdk:17.0.2

WORKDIR /app

COPY target/finco-0.0.1-SNAPSHOT.jar .

EXPOSE 8086

ENTRYPOINT [ "java", "-jar", "finco-0.0.1-SNAPSHOT.jar" ]