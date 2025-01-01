FROM maven:3.9.5  as build
WORKDIR /app
COPY pom.xml .
COPY src .
RUN mvn clean package -DskipTests -e

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]