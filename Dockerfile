# Use official base image of Java Runtim
FROM openjdk:8-jdk-alpine

# Set volume point to /tmp
RUN mkdir /db
VOLUME /db

VOLUME /tmp

RUN apk add tzdata
RUN cp /usr/share/zoneinfo/Asia/Bangkok /etc/localtime
RUN apk add --no-cache ttf-dejavu
EXPOSE 8070

# Set application's JAR file
ARG JAR_FILE=reportexporter.jar

ADD target/${JAR_FILE} app.jar

# COPY THSarabunNew.jar /usr/lib/jvm/java-1.8-openjdk/jre/lib/ext/

COPY THSarabunNew.jar /usr/lib/jvm/java-1.8-openjdk/jre/lib/ext

ENTRYPOINT ["java", "-jar", "/app.jar"]
