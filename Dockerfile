# syntax=docker/dockerfile:1

################################################################################
FROM eclipse-temurin:21-jdk-jammy AS deps
WORKDIR /build
COPY --chmod=0755 mvnw mvnw

COPY pom.xml mvnw ./
COPY .mvn/ .mvn/

RUN chmod +x mvnw
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -DskipTests

################################################################################
FROM deps AS package
WORKDIR /build
COPY ./src src/
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################
FROM package AS extract
WORKDIR /build
RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################
FROM eclipse-temurin:21-jre-jammy AS final

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

WORKDIR /app
USER appuser

COPY --from=extract /build/target/extracted/dependencies/           ./dependencies/
COPY --from=extract /build/target/extracted/spring-boot-loader/    ./spring-boot-loader/
COPY --from=extract /build/target/extracted/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=extract /build/target/extracted/application/           ./application/

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
