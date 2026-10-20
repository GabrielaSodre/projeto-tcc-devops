FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
RUN addgroup -S app && adduser -S app -G app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
