FROM openjdk:21
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} prinfoservice.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/prinfoservice.jar"]