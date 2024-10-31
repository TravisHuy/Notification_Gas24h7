FROM maven:3.8.4-openjdk-17 as build
WORKDIR /app
COPY . /app/

# Tạo thư mục resources nếu chưa tồn tại
RUN mkdir -p src/main/resources/

# Copy file JSON vào container trong quá trình build
COPY test.json src/main/resources/test.json

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

# Copy JAR file từ stage build
COPY --from=build /app/target/*.jar app.jar

# Copy file JSON từ stage build
COPY --from=build /app/src/main/resources/test.json /test.json

ENV FIREBASE_CREDENTIALS_PATH=/test.json
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]