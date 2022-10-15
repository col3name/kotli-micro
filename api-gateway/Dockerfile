FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY api-gateway/build/libs/* build/libs/
COPY api-gateway/build/libs/api-gateway-0.0.1-SNAPSHOT.jar /app/build/app.jar

WORKDIR /app/build
EXPOSE 8000:8000

ENTRYPOINT java -jar /app/build/app.jar