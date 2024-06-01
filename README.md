# BidBanker

This project is a multi-module Gradle setup for the BidBanker platform. It includes two main services:
- Users Service: Manages user registration, login, and token issuance.
- Auctions Service: Manages auctions, bids, and uses Spring Security with OAuth2 for JWT validation.

## Project Structure

- `common`: Shared utilities and configurations.
- `users-service`: Manages users and authentication.
- `auctions-service`: Manages auctions and bids.

## Getting Started

### Prerequisites

- Java 17
- Gradle 7.x

### Building the Project

```sh
./gradlew build
```