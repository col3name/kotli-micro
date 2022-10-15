#FROM gradle:jdk17 as builder
#WORKDIR /app
#COPY product/src ./src
#COPY product/build.gradle.kts ./build.gradle.kts
#COPY product/Dockerfile product/Dockerfile
#RUN gradle clean build

FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY product/build/libs/* build/libs/
COPY product/build/libs/product-0.0.1-SNAPSHOT.jar /app/build/app.jar

WORKDIR /app/build
EXPOSE 8080:8080

ENTRYPOINT java -jar /app/build/app.jar
#FROM openjdk:17-alpine

#COPY /Users/mikhailmikushov/Desktop/github/KazanExpress/product/build/libs/s ./app.jar
#EXPOSE 8080:8080
# RUN java -jar app.jar