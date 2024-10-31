#
# Build stage
#
FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app
COPY . /app/
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /app/target/*.jar app.jar

COPY src/main/resources/test.json /test.json
ENV FIREBASE_CREDENTIALS_PATH=/test.json
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]