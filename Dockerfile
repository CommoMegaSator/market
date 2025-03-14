FROM openjdk:21-jdk-slim-bullseye
MAINTAINER CommoMegaSator
VOLUME /tmp
EXPOSE 5003
RUN mkdir -p /app/logs/

RUN apt-get update && apt-get install -y maven
COPY pom.xml .
RUN mvn -f pom.xml dependency:resolve
COPY src ./src
RUN mvn -f pom.xml clean install
RUN ls /target

ENTRYPOINT ["java", "-jar", "/target/task.jar"]