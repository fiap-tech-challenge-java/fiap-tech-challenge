# syntax=docker/dockerfile:1

################################################################################
# 1. Build dependencies and go-offline
FROM eclipse-temurin:21-jdk-jammy AS deps
WORKDIR /build
COPY mvnw pom.xml ./
COPY .mvn/ .mvn/
RUN chmod +x mvnw
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -B

################################################################################
# 2. Compile, generate sources (OpenAPI) and package
FROM deps AS builder
WORKDIR /build
COPY src/ src/
# Executa clean, gera as fontes do OpenAPI na fase generate-sources e empacota
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw clean generate-sources package -DskipTests -B \
    && mv target/*.jar target/app.jar

################################################################################
# 3. Extrai camadas do JAR (opcional, se usar layer tools)
FROM builder AS extract
WORKDIR /build
RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################
# 4. Imagem final apenas com JRE
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

USER appuser
WORKDIR /app

COPY --from=extract /build/target/extracted/dependencies/           ./dependencies/
COPY --from=extract /build/target/extracted/spring-boot-loader/    ./spring-boot-loader/
COPY --from=extract /build/target/extracted/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=extract /build/target/extracted/application/           ./application/

EXPOSE 8080
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]
