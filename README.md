# TechChallenge

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **Sistema Unificado de Gestão de Restaurantes** – Backend para gerenciamento de usuários (clientes e donos de restaurante) em Spring Boot

## Índice

* [Descrição](#descrição)
* [Contribuidores](#contribuidores)
* [Tecnologias](#tecnologias)
* [Arquitetura](#arquitetura)
* [Endpoints da API](#endpoints-da-api)
* [Pré-requisitos](#pré-requisitos)
* [Configuração e Execução](#configuração-e-execução)

  * [1. Clone do repositório](#1-clone-do-repositório)
  * [2. Exemplo de `.env`](#2-exemplo-de-env)
  * [3. Docker Compose](#3-docker-compose)
  * [4. Execução no IntelliJ IDEA (opcional)](#4-execução-no-intellij-idea-opcional)
* [Documentação Swagger](#documentação-swagger)
* [Collection de Testes (Postman)](#collection-de-testes-postman)
* [Qualidade de Código](#qualidade-de-código)
* [Licença](#licença)

---

## Descrição

TechChallenge é uma API RESTful desenvolvida em **Java 21** com **Spring Boot 3** para o gerenciamento de usuários (cadastro, atualização, exclusão lógica e autenticação) de um sistema unificado de restaurantes. Suporta dois perfis de usuário:

* **OWNER**: donos de restaurante, com permissão para visualizar clientes.
* **CUSTOMER**: clientes, com acesso restrito aos próprios dados.

A aplicação utiliza **JWT** para autenticação e segue a **arquitetura hexagonal (ports & adapters)**, tornando o código modular, testável e de fácil manutenção.

## Contribuidores

  * https://github.com/joaodamasceno2001
  * https://github.com/Lari-Lucena
  * https://github.com/TeT95
  * https://github.com/matheuscarvalheira
  * https://github.com/rebecanonato89

## Tecnologias

* **Spring Boot 3** (Java 21)
* **Spring Web**, **Spring Data JPA**, **Spring Security**, **Spring Actuator**
* **PostgreSQL 15**
* **JWT** (JJWT + Spring Security)
* **Docker** e **Docker Compose**
* **Lombok**, **MapStruct**, **JUnit 5**, **Spring Boot Test**

## Arquitetura

O projeto adota a **arquitetura hexagonal**, com separação em:

* **Domain**: entidades e regras de negócio.
* **Application**: casos de uso e serviços.
* **Adapters de Entrada**: controllers REST gerados via OpenAPI.
* **Adapters de Saída**: repositórios JPA para persistência em PostgreSQL.

![Figura 1 – Arquitetura Hexagonal](./docs/architecture.png)

## Endpoints da API

| Método | Path                              | Descrição                                                 |
| -----: | --------------------------------- | --------------------------------------------------------- |
|   POST | `/api/v1/auth/login`              | Autentica usuário e retorna token JWT                     |
|   POST | `/api/v1/users`                   | Cria novo usuário (OWNER ou CUSTOMER)                     |
|    GET | `/api/v1/users`                   | Lista usuários (OWNER vê todos, CUSTOMER vê só o próprio) |
|    GET | `/api/v1/users/{id}`              | Busca usuário por ID                                      |
|    PUT | `/api/v1/users/{id}`              | Atualiza dados de usuário                                 |
| DELETE | `/api/v1/users/{id}`              | Exclui logicamente usuário                                |
|    PUT | `/api/v1/users/change-password`   | Troca senha de usuário                                    |
|    GET | `/api/v1/users/{id}/addresses`    | Lista endereços de um usuário                             |
|   POST | `/api/v1/users/{id}/addresses`    | Adiciona novo endereço                                    |
|    PUT | `/api/v1/users/{u}/addresses/{a}` | Atualiza endereço existente                               |
| DELETE | `/api/v1/users/{u}/addresses/{a}` | Remove logicamente um endereço                            |

> **Observação:** Protegido por JWT. Inclua o header `Authorization: Bearer <token>` nos requests.

## Pré-requisitos

* Docker (Engine + Compose)
* Java JDK 21
* (Opcional) IntelliJ IDEA ou outra IDE Java

## Configuração e Execução

### 1. Clone do repositório

```bash
git clone https://github.com/fiap-tech-challenge-java/fiap-tech-challenge.git
cd fiap-tech-challenge
```

### 2. Exemplo de `.env`

Crie na raiz um arquivo `.env` com o conteúdo:

> **Atenção:** Nunca utilize valores fracos ou padrões para `JWT_SECRET`. Gere uma chave secreta forte e única para cada ambiente.  
> Exemplo de comando para gerar uma chave segura (Linux/macOS):  
> ```bash
> openssl rand -base64 32
> ```

```ini
DB_NAME=techchallenge
DB_USERNAME=postgres
DB_PASSWORD=postgrespass
DB_PORT=5438
DB_URL=jdbc:postgresql://techchallenge_postgres:5432/techchallenge
JWT_SECRET=2qQw8v9JkL1pXz3sT6bN0eR5uYhG7cV2mQw8v9JkL1pXz3sT6bN0eR5uYhG7cV2m
```

### 3. Docker Compose

**Suba os containers:**

```bash
docker compose up --build
```

* A aplicação estará disponível em `http://localhost:8080`.
* O banco PostgreSQL escuta na porta `5438` do host.

### 4. Execução no IntelliJ IDEA (opcional)

1. Abra o projeto no IntelliJ.
2. Configure o SDK Java 21 em `File > Project Structure`.
3. Em `Run > Edit Configurations`, adicione uma configuração Spring Boot:

   * Target: Docker Compose (`compose.yaml`), serviço `techchallenge_application`.
4. Execute `TechChallengeApplication`.

## Documentação Swagger

Acesse **Swagger UI** em:

```
http://localhost:8080/api/v1/swagger-ui/index.html#/
```

## Collection de Testes (Postman)

Importe `TechChallenge.postman_collection.json` (na raiz) no Postman para testar todos os endpoints.

## Qualidade de Código

* **SOLID** e **DRY**
* **DTOs** e **mapeamento** via MapStruct
* **Tratamento global** de exceções com `@ControllerAdvice`
* **Testes**: JUnit 5 (unitários e de integração)
* **Segurança**: hashing de senhas (BCrypt) e JWT

## Licença

Este projeto está licenciado sob a **MIT License** – consulte o arquivo [LICENSE](LICENSE) para detalhes.
