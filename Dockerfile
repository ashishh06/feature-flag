FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN ./gradlew :flag-server:bootJar

EXPOSE 8080

CMD ["java", "-jar", "flag-server/build/libs/flag-server-0.0.1-SNAPSHOT.jar"]