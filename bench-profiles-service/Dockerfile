FROM maven:3.9-amazoncorretto-21

WORKDIR /bench-profiles-service
COPY . .
RUN mvn clean install -DskipTests

CMD mvn spring-boot:run