FROM openjdk:11-jre
COPY target/backend-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
