FROM gradle:8.5-jdk21 AS build

ARG USERNAME
ARG TOKEN

ENV GIT_TOKEN=${GIT_TOKEN}
ENV GITHUB_ACTOR ${USERNAME}
ENV TOKEN ${TOKEN}


COPY  . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble

FROM amazoncorretto:21-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production","/app/spring-boot-application.jar"]