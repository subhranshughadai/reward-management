# Customer Rewards Points Calculator

> A Spring Boot REST API that calculates reward points for customers based on their purchase transactions over the last three months.

## Table of Contents

- [Overview](#overview)
- [Business Requirements](#business-requirements)
- [Technology Stack](#technology-stack)
- [Application Architecture](#application-architecture)
- [Setup Instructions](#setup-instructions)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Database Schema](#database-schema)
- [Configuration](#configuration)
- [Project Highlights](#project-highlights)

---

## Overview

The application exposes endpoints to:

- **Calculate reward points** - Automatic calculation based on spending tiers
- **View reward summaries** - Monthly breakdown and total points per customer
- **Pagination support** - Efficiently retrieve large datasets
- **Caching** - Improved performance for repeated queries

---

## Business Requirements

### Reward Calculation Rules

Reward points are calculated based on the following rules:

| Spending Range | Points Earned |
|----------------|---------------|
| $0 – $50 | 0 points |
| $50.01 – $100 | 1 point for every dollar over $50 |
| Above $100 | 2 points for every dollar over $100 + 1 point for every dollar between $50 and $100 |

### Example Calculations

| Amount | Calculation | Points |
|--------|-------------|--------|
| $40 | No points | **0** |
| $70 | (70 − 50) × 1 | **20** |
| $100 | (100 − 50) × 1 | **50** |
| $120 | (20 × 2) + (50 × 1) | **90** |
| $1000 | (900 × 2) + (50 × 1) | **1850** |

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|----------|
| Java | 17 | Programming language |
| Spring Boot | 3.2.5 | Application framework |
| Spring Data JPA | 3.2.5 | ORM and database abstraction |
| Spring Validation | 3.2.5 | Request validation |
| Spring Cache | 3.2.5 | Caching abstraction |
| PostgreSQL | 14+ | Application database |
| Maven | 3.6+ | Build tool |
| Lombok | 1.18.30 | Reduce boilerplate code |
| JUnit 5 & Mockito | Latest | Unit testing |
| SpringDoc OpenAPI | 2.5.0 | Swagger API documentation |

---

## Application Architecture

The project follows a **layered architecture**:

| Layer | Package | Description |
|-------|---------|-------------|
| Controller | `controller` | REST endpoints |
| Service | `service` | Business logic |
| Repository | `repository` | Database access |
| Entity | `entity` | JPA entities |
| DTO | `dto` | API request/response objects |
| Util | `util` | Reward calculation logic |
| Config | `config` | Application configuration |
| Exception | `exception` | Global exception handling |

### Project Structure

```
rewardmanagement/
│
├── src/main/java/com/charter/rewardmanagement/
│   ├── config/
│   │   └── SwaggerConfig.java
│   ├── controller/
│   │   └── RewardController.java
│   ├── service/
│   │   ├── RewardService.java
│   │   └── RewardServiceImpl.java
│   ├── repository/
│   │   └── TransactionRepository.java
│   ├── entity/
│   │   └── Transaction.java
│   ├── dto/
│   │   ├── MonthlyReward.java
│   │   └── RewardResponse.java
│   ├── util/
│   │   └── RewardCalculator.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   └── ResourceNotFoundException.java
│   └── RewardmanagementApplication.java
│
├── src/main/resources/
│   ├── application.properties
│   └── data.sql
│
├── src/test/java/com/charter/rewardmanagement/
│   ├── controller/
│   │   └── RewardControllerTest.java
│   ├── repository/
│   │   └── TransactionRepositoryTest.java
│   ├── service/
│   │   └── RewardServiceTest.java
│   ├── util/
│   │   └── RewardCalculatorTest.java
│   ├── RewardmanagementApplicationTests.java
│   └── RewardmanagementIntegrationTests.java
│
├── pom.xml
└── README.md
```

---

## Setup Instructions

### Prerequisites

Make sure the following tools are installed:

- Java 17+
- Maven 3.6+
- PostgreSQL 14+

**Check versions:**

```bash
java -version
mvn -version
psql --version
```

### Installation

**1. Clone the repository:**

```bash
git clone <repository-url>
cd reward-management-main
```

**2. Create the PostgreSQL database:**

```sql
CREATE DATABASE rewardsdb;
```

**3. Update database credentials** (if needed) in:

```
src/main/resources/application.properties
```

```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

**4. Build the project:**

```bash
mvn clean install
```

**5. Run the application:**

```bash
mvn spring-boot:run
```

**6. Access the application:**

- **Application:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **API Docs (JSON):** http://localhost:8080/v3/api-docs

---

## API Documentation

### Swagger UI

Interactive API documentation is available at:

**http://localhost:8080/swagger-ui/index.html**

### API Endpoints

#### Get Rewards Summary

**Endpoint:** `GET /api/rewards`

**Description:** Retrieves reward points for all customers with monthly breakdown over a specified date range.

**Query Parameters:**
- `start` - Start date (format: yyyy-MM-dd)
- `end` - End date (format: yyyy-MM-dd)
- `page` - Page number (default: 0)
- `size` - Page size (default: 5)

**Example:**

```bash
GET /api/rewards?start=2024-01-01&end=2024-03-31&page=0&size=5
```

**Response:**

```json
{
  "content": [
    {
      "customerId": 1,
      "customerName": "John Doe",
      "monthlyRewards": [
        {
          "month": "2024-01",
          "points": 150
        },
        {
          "month": "2024-02",
          "points": 200
        }
      ],
      "totalPoints": 350
    }
  ],
  "pageable": {...},
  "totalElements": 10,
  "totalPages": 2
}
```

---

## Testing

The project includes comprehensive unit tests and integration tests.

### Test Coverage

| Test Class | Type | Purpose |
|------------|------|----------|
| `RewardmanagementApplicationTests` | Unit | Application context loading |
| `RewardCalculatorTest` | Unit | Reward calculation logic |
| `RewardServiceTest` | Unit | Service layer testing |
| `TransactionRepositoryTest` | Unit | Repository testing |
| `RewardControllerTest` | Unit | Rewards API endpoints |
| `RewardmanagementIntegrationTests` | Integration | End-to-end rewards flow |

### Running Tests

**Run all tests:**

```bash
mvn test
```

**Run specific test class:**

```bash
mvn test -Dtest=RewardCalculatorTest
```

**Run tests with coverage report:**

```bash
mvn clean test
```

---

## Database Schema

### Transaction Table

| Column | Type | Description |
|--------|------|-------------|
| id | Long | Primary key |
| customer_id | Long | Customer identifier |
| customer_name | String | Customer name |
| amount | Double | Transaction amount |
| transaction_date | LocalDate | Date of transaction |

### Sample Data

The application automatically loads sample data on startup from:

```
src/main/resources/data.sql
```

This allows testing the reward calculation endpoints immediately after startup without manual data entry.

---

## Configuration

### Application Properties

Key configurations in `src/main/resources/application.properties`:

**Server Configuration:**
```properties
spring.application.name=rewardmanagement
```

**Database Configuration:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rewardsdb
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=org.postgresql.Driver
```

**JPA Configuration:**
```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.defer-datasource-initialization=true
```

**Cache Configuration:**
```properties
spring.cache.type=simple
```

**Data Initialization:**
```properties
spring.sql.init.mode=always
spring.sql.init.platform=postgres
```

---

## Project Highlights

- **Layered Architecture** - Clean separation of concerns
- **Pagination Support** - Efficient handling of large datasets
- **Caching** - Improved performance for repeated queries
- **Swagger Documentation** - Interactive API testing
- **Global Exception Handling** - Consistent error responses
- **Comprehensive Testing** - Unit and integration tests
- **Dynamic Date Calculation** - No hardcoded months
- **Sample Data** - Pre-loaded test data for quick start

---

## Troubleshooting

**Issue: Application fails to start**
- Ensure PostgreSQL is running on port 5432
- Verify database `rewardsdb` exists
- Check database credentials in `application.properties`

**Issue: Tests fail**
- Ensure all dependencies are downloaded: `mvn clean install`
- Check Java version: `java -version` (should be 17+)

**Issue: Swagger UI not accessible**
- Verify application is running on http://localhost:8080
- Try accessing http://localhost:8080/swagger-ui/index.html
- Check SpringDoc dependency in `pom.xml`
