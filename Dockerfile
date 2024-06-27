FROM gradle:8.5-jdk21 AS build

ARG USERNAME
ARG GIT_TOKEN

ENV GIT_TOKEN=${GIT_TOKEN}
ENV GITHUB_ACTOR ${USERNAME}



COPY  . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble

FROM amazoncorretto:21-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
RUN mkdir -p /usr/local/newrelic
ADD ./newrelic-java/newrelic/newrelic.jar /usr/local/newrelic/newrelic.jar
ADD ./newrelic-java/newrelic/newrelic.yml /usr/local/newrelic/newrelic.yml

ENTRYPOINT ["java", "-jar","-javaagent:/usr/local/newrelic/newrelic.jar","-Dspring.profiles.active=production","/app/spring-boot-application.jar"]