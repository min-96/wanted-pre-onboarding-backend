FROM gradle:jdk17 as builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle clean build -x test

FROM openjdk:17-jdk

EXPOSE 8080

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/wanted-pre-onboarding-backend.jar
COPY ./wait-for-it.sh /app/wait-for-it.sh

RUN chmod +x /app/wait-for-it.sh

