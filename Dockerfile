FROM eclipse-temurin:21-jdk

LABEL author="hikinokouji"

WORKDIR /app

COPY build/libs/CoffeFlashBackend-0.0.1-SNAPSHOT-plain.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]