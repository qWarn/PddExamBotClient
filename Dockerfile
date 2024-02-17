FROM openjdk:21

WORKDIR app

COPY .mvn .mvn
COPY src src
COPY pom.xml .
COPY mvnw .

RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "/app/target/PddExamBotClient-0.0.1-SNAPSHOT.jar"]



