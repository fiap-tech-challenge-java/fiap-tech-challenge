# syntax=docker/dockerfile:1

################################################################################
# 1. Build dependencies and go-offline
FROM eclipse-temurin:21-jdk-jammy AS deps
WORKDIR /build

# Copia wrapper e POM
COPY mvnw pom.xml ./
COPY .mvn/ .mvn/

# Torna o wrapper executável
RUN chmod +x mvnw

# Baixa dependências para offline (BuildKit cache)
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -B

################################################################################
# 2. Compile, generate sources (OpenAPI) and package
FROM deps AS builder
WORKDIR /build

# Copia TODO o projeto (fonte, specs, configs, mvnw, .mvn etc)
COPY . .

# Executa clean → compile → generate-sources → package
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw clean compile generate-sources package -DskipTests -B \
    && mv target/*.jar target/app.jar

################################################################################
# 3. Extrai camadas do JAR (opcional)
FROM builder AS extract
WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################
# 4. Imagem final apenas com JRE
FROM eclipse-temurin:21-jre-jammy AS final
ARG UID=10001

# Cria usuário sem privilégios
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

# Copia camadas extraídas para otimizar rebuilds
COPY --from=extract /build/target/extracted/dependencies/           ./dependencies/
COPY --from=extract /build/target/extracted/spring-boot-loader/    ./spring-boot-loader/
COPY --from=extract /build/target/extracted/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=extract /build/target/extracted/application/           ./application/

EXPOSE 8080

ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]
