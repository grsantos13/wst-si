FROM openjdk:11
ARG JAR_FILE=build/libs/*all.jar
COPY ${JAR_FILE} wst-si.jar
ENTRYPOINT ["java","-jar","/wst-si.jar"]