# --- Build stage -------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

COPY src ./src
RUN ./mvnw -B clean package -DskipTests

# --- Runtime stage -------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S devgraph && adduser -S devgraph -G devgraph
COPY --from=build /app/target/devgraph-*.jar app.jar
RUN chown devgraph:devgraph app.jar
USER devgraph

EXPOSE 8080

# NEO4J_URI, NEO4J_USERNAME, NEO4J_PASSWORD and friends (see .env.example)
# must be supplied at `docker run` time - there are no real defaults baked
# into the image.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
