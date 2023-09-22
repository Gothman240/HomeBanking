FROM gradle:8.2.1-jdk11-alpine

COPY . .

RUN gradle build

EXPOSE 8080