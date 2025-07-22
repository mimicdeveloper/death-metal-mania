FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/death-metal-mania-1.0.jar death-metal-mania-1.0.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "death-metal-mania-1.0.jar"]