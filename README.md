# Rewards Program API

## Overview
Spring Boot application that calculates customer reward points based on transactions within a three-month period.

## Tech Stack
- Java 17
- Spring Boot 3.2.5
- PostgreSQL
- Maven
- Swagger (OpenAPI)
- JUnit 5
- Mockito
- Cache abstraction

## Reward Rules
- 2 points for every dollar spent over $100
- 1 point for every dollar between $50 and $100
- No points for amount <= $50

## API Endpoint
GET /api/rewards?start=yyyy-MM-dd&end=yyyy-MM-dd&page=0&size=5

## Features
- Pagination
- Caching
- Swagger documentation
- Global exception handling
- Unit & integration tests
- No hardcoded months (derived from transaction date)

## Run Application
mvn clean install
mvn spring-boot:run

Swagger:
http://localhost:8080/swagger-ui/index.html
