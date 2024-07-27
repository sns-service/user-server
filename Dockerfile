FROM openjdk:21-jdk-slim

COPY build/libs/userserver-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
ENV SPRING_PROFILES_ACTIVE=local

ENV TZ=Asia/Seoul
