FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY order/build/libs/* build/libs/
COPY order/build/libs/order-0.0.1-SNAPSHOT.jar /app/build/app.jar

WORKDIR /app/build
EXPOSE 8082:8082

ENTRYPOINT java -jar /app/build/app.jar