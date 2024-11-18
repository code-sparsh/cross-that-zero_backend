FROM maven:3.8.4-openjdk-17

WORKDIR /app

COPY pom.xml .

COPY . .

RUN mvn clean package -DskipTests

CMD ["java", "-DPROFILE=prod", "-jar", "target/CrossThatZero-backend-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080 9000