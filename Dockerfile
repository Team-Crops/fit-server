FROM openjdk:11

ARG PROFILE
ARG TZ=Asia/Seoul
ARG JAR_FILE=./build/libs/app.jar)
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD
ARG JWT_ACCESS_KEY
ARG JWT_REFRESH_KEY
ARG URL_ENCODED_CONTENT_TYPE
ARG GRANT_TYPE
ARG KAKAO_CLIENT_ID
ARG KAKAO_LOGIN_PAGE_URL
ARG GOOGLE_CLIENT_ID
ARG GOOGLE_CLIENT_SECRET
ARG GOOGLE_LOGIN_PAGE_URL

COPY ${JAR_FILE} app.jar

ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV JWT_ACCESS_KEY=${JWT_ACCESS_KEY}
ENV JWT_REFRESH_KEY=${JWT_REFRESH_KEY}
ENV URL_ENCODED_CONTENT_TYPE=${URL_ENCODED_CONTENT_TYPE}
ENV GRANT_TYPE=${GRANT_TYPE}
ENV KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID}
ENV KAKAO_LOGIN_PAGE_URL=${KAKAO_LOGIN_PAGE_URL}
ENV GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
ENV GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
ENV GOOGLE_LOGIN_PAGE_URL=${GOOGLE_LOGIN_PAGE_URL

EXPOSE 8080
ENTRYPOINT ["java","-jar","-Duser.timezone=${TZ}","-Dspring.profiles.active=${PROFILE}","app.jar"]