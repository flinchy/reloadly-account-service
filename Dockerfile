FROM openjdk:11

EXPOSE 8081

ADD ./build/libs/*.jar account-service.jar

ENTRYPOINT ["java","-jar","/account-service.jar"]
