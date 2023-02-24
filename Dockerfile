FROM openjdk:19
EXPOSE 8080
RUN mvn package
COPY target/Five-o-gram-0.0.1-SNAPSHOT.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
