# Users Service

The `users-service` is a part of the BidBanker application that manages user registration and retrieval. It is built using Spring Boot, R2DBC, and H2 database with Liquibase for database migrations. The service handles user-related operations such as registering new users.

## Features

- Register new users

## Technologies

- Java 17
- Spring Boot
- Spring WebFlux
- H2 Database
- Liquibase
- Project Lombok

## Prerequisites

- Java 17
- Gradle
- An IDE like IntelliJ IDEA 

## Setup

**Build the project**:

```bash
./gradlew users-service:build
```

**Run the service**:

```bash
./gradlew users-service:bootRun
```

## API Testing


[Users.http](Users.http) has common API Requests for testing against local endpoints