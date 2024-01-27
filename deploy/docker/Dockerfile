FROM openjdk:17

ARG PROFILE
ARG TZ=Asia/Seoul
ARG JAR_FILE=./build/libs/app.jar
COPY ${JAR_FILE} app.jar
ENV PROFILE=${PROFILE}
EXPOSE 8080
EXPOSE 9000
ENTRYPOINT ["java","-jar","-Duser.timezone=${TZ}","-Dspring.profiles.active=${PROFILE}","app.jar"]