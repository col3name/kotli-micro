FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY customer/build/libs/* build/libs/
COPY customer/build/libs/customer-0.0.1-SNAPSHOT.jar /app/build/aeé´pp.jar

WORKDIR /app/build
EXPOSE 8081:8081

ENTRYPOINT java -jar /app/build/app.jar