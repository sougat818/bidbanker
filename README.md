
# BidBanker

This project is a multi-module Gradle setup for the BidBanker platform. It includes two main services:
- Users Service: Manages user registration and authentication.
- Auctions Service: Manages auctions and bids.

## Project Structure

- `common`: Shared utilities and configurations.
- `users-service`: Manages users and authentication.
- `auctions-service`: Manages auctions and bids.

## Getting Started

### Prerequisites

- Java 17
- Gradle
- An IDE like IntelliJ IDEA

### Setup Guide

1. **Clone the Repository**

   ```bash
   git clone https://github.com/sougat818/BidBanker.git
   cd BidBanker
   ```

2. **Import Project into IDE**

    - Open IntelliJ IDEA.
    - Select `File > Open...` and choose the `BidBanker` directory.
    - IntelliJ will automatically detect the Gradle project and import it.

3. **Configure Environment**

    - Ensure Java 17 is installed and configured in your IDE.
    - Ensure Gradle is installed and configured in your IDE.

4. **Build the Project**

   To build the services, use the following commands:

   ```bash
   ./gradlew users-service:build
   ./gradlew auctions-service:build
   ```

5. **Run the Services Locally**

   To run the services on your local machine:

   ```bash
   ./gradlew users-service:bootRun
   ./gradlew auctions-service:bootRun
   ```

   To run the services with the production configuration:

   ```bash
   ./gradlew users-service:bootRun --args='--spring.profiles.active=prod'
   ./gradlew auctions-service:bootRun --args='--spring.profiles.active=prod'
   ```

6. **Testing the API**

   Use the provided [Users.http](Users.http) and [Auctions.http](Auctions.http) files for testing the API endpoints against local instances.

### Running Tests

To run unit tests for both services, use the following commands:

```bash
./gradlew users-service:check
./gradlew auctions-service:check
```

## Requirements

- Buyers are shown products that they can bid for, and a minimum price is set by the seller.
- The buyer can submit any number of bids, which will be rejected if they fall below the seller's minimum price.
- At the end of the auction, the first buyer who bid the highest amount will win the auction and purchase the product.

### Use-Cases

- As a Seller, I want to be able to register a new product for auction and specify a minimum bid.
- As a Buyer, I want to bid in an auction any number of times.
- As a Seller, I want to be able to end the auction and see the winner and their bid.

### Testing

The code should have appropriate levels of testing to ensure that the stated requirements are met.

## TODO

- Finish Implementation of user authentication. [feature/jwt-asymmetric-authentication](https://github.com/sougat818/bidbanker/pull/3)
- Add more comprehensive API documentation.
- Add static code analysis (Checkstyle, PMD, SpotBugs).
- Set up and configure code coverage reporting.
- Improve integration tests.

## Technologies

- Java 17
- Spring Boot
- Spring WebFlux
- H2 Database
- Liquibase
- Project Lombok
