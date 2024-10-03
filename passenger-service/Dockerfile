FROM openjdk:17-slim
ADD target/passenger-service-0.0.1-SNAPSHOT.jar passenger-service.jar
ENTRYPOINT ["java", "-jar", "passenger-service.jar"]