FROM openjdk:19 AS builder
RUN microdnf install findutils
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:19
COPY --from=builder build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE} -DjasyptPassword=${JASYPT_PASSWORD}", "-jar", "/app.jar"]