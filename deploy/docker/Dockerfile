FROM openjdk:17

ARG PROFILE
ARG TZ=Asia/Seoul
ARG JAR_FILE=./build/libs/app.jar

#RUN useradd -m -u 1000 ubuntu_user
COPY ${JAR_FILE} app.jar
#USER ubuntu_user

EXPOSE 8080
ENTRYPOINT ["java","-jar","-Duser.timezone=${TZ}","-Dspring.profiles.active=${PROFILE}","app.jar"]