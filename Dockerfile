FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:resolve -B -q

COPY src/ src/
RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

COPY --from=builder /app/target/my-agent-travel-planner-app-*.jar app.jar

VOLUME /app/data

ENTRYPOINT ["java", "-jar", "app.jar"]
