FROM openjdk:8-jdk-alpine AS base
WORKDIR /app
EXPOSE 9090

FROM maven:3.5.4-jdk-8-alpine AS build
ARG APP_VERSION
WORKDIR /app
COPY . .
RUN mvn versions:set -DnewVersion=${APP_VERSION}
#RUN mvn clean package
RUN ["mvn", "install", "-Dmaven.test.skip=true"]

FROM base AS final
ARG APP_VERSION
WORKDIR /app
#COPY --from=build /app/target/reservations.api-${APP_VERSION}.jar ./app.jar
COPY --from=build /app/target/reservations.api-*.jar ./app.jar
#ENTRYPOINT ["java","-Djava.security.edg=file:/dev/./urandom","-jar","./app.jar"]
#ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-jar","./app.jar"]
ENTRYPOINT ["java","-jar","./app.jar"]
