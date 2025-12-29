# Customer Service - Project Structure

## Complete Directory Layout

```
CustomerService/
│
├── backend/                                    # Spring Boot microservice
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── customerservice/
│   │   │   │           ├── config/
│   │   │   │           │   └── SecurityConfig.java          # Spring Security & CORS config
│   │   │   │           │
│   │   │   │           ├── controller/
│   │   │   │           │   └── CustomerController.java      # REST API endpoints
│   │   │   │           │       ├── POST   /api/customers
│   │   │   │           │       ├── GET    /api/customers
│   │   │   │           │       ├── GET    /api/customers/{id}
│   │   │   │           │       ├── GET    /api/customers/regno/{customerRegNo}
│   │   │   │           │       ├── PUT    /api/customers/{id}
│   │   │   │           │       └── DELETE /api/customers/{id}
│   │   │   │           │
│   │   │   │           ├── exception/
│   │   │   │           │   ├── GlobalExceptionHandler.java  # Centralized error handling
│   │   │   │           │   └── ResourceNotFoundException.java # Custom exception
│   │   │   │           │
│   │   │   │           ├── model/
│   │   │   │           │   └── Customer.java                # Entity/Model
│   │   │   │           │       ├── id (PK)
│   │   │   │           │       ├── customerRegNo (unique)
│   │   │   │           │       ├── name
│   │   │   │           │       ├── email
│   │   │   │           │       ├── contactNumber
│   │   │   │           │       └── address
│   │   │   │           │
│   │   │   │           ├── repository/
│   │   │   │           │   └── CustomerRepository.java      # JPA Repository (DAO)
│   │   │   │           │       ├── save()
│   │   │   │           │       ├── findAll()
│   │   │   │           │       ├── findById()
│   │   │   │           │       ├── delete()
│   │   │   │           │       └── findByCustomerRegNo()
│   │   │   │           │
│   │   │   │           ├── service/
│   │   │   │           │   └── CustomerService.java         # Business Logic Layer
│   │   │   │           │       ├── createCustomer()
│   │   │   │           │       ├── getAllCustomers()
│   │   │   │           │       ├── getCustomerById()
│   │   │   │           │       ├── updateCustomer()
│   │   │   │           │       ├── deleteCustomer()
│   │   │   │           │       └── getCustomerByRegNo()
│   │   │   │           │
│   │   │   │           └── CustomerServiceApplication.java  # Main Spring Boot class
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.properties                   # Main configuration
│   │   │       │   ├── server.port=8081
│   │   │       │   ├── spring.datasource.url
│   │   │       │   ├── spring.datasource.username
│   │   │       │   ├── spring.datasource.password
│   │   │       │   ├── spring.jpa.hibernate.ddl-auto
│   │   │       │   └── spring.jpa.show-sql
│   │   │       │
│   │   │       └── application-dev.properties              # Development profile
│   │   │           ├── Extended logging
│   │   │           ├── SQL formatting
│   │   │           └── Debug mode
│   │   │
│   │   └── test/
│   │       └── java/
│   │           └── com/
│   │               └── customerservice/
│   │                   ├── CustomerServiceApplicationTests.java
│   │                   ├── controller/
│   │                   │   └── CustomerControllerTest.java  # Controller unit tests
│   │                   └── service/
│   │                       └── CustomerServiceTest.java     # Service unit tests
│   │
│   ├── pom.xml                                # Maven configuration
│   │   ├── Parent: spring-boot-starter-parent v3.4.3
│   │   ├── Java: 21
│   │   ├── Dependencies:
│   │   │   ├── spring-boot-starter-web
│   │   │   ├── spring-boot-starter-data-jpa
│   │   │   ├── spring-boot-starter-validation
│   │   │   ├── spring-boot-starter-security
│   │   │   ├── mysql-connector-java
│   │   │   ├── lombok
│   │   │   ├── spring-cloud-starter-openfeign
│   │   │   ├── spring-boot-devtools
│   │   │   └── Testing libraries (junit, mockito)
│   │   └── Build plugins: maven-compiler-plugin, maven-jar-plugin
│   │
│   ├── mvnw & mvnw.cmd                        # Maven wrapper (Windows & Unix)
│   ├── Dockerfile                             # Docker image build
│   ├── .dockerignore                          # Docker ignore rules
│   ├── CONTRIBUTING.md                        # Contribution guidelines
│   ├── DEPLOYMENT.md                          # Deployment guide
│   └── target/                                # Compiled output (generated)
│       ├── classes/                           # Compiled Java classes
│       ├── customer-service-1.0.0.jar         # Built JAR file
│       └── test-classes/                      # Compiled test classes
│
├── README.md                                  # Project documentation
├── ARCHITECTURE.md                            # System architecture & design
├── docker-compose.yml                         # Docker Compose configuration
├── .gitignore                                 # Git ignore rules
├── .env.example                               # Environment variables template
└── PROJECT_STRUCTURE.md                       # This file

```

## Layer Architecture

```
┌─────────────────────────────────────────────────────┐
│            REST API Layer (Controller)              │
│  Handles: HTTP requests/responses, routing          │
│  Port: 8081                                         │
│  Base Path: /api/customers                          │
└────────────────┬────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────┐
│         Business Logic Layer (Service)              │
│  Handles: Validation, business rules, transactions  │
│  Class: CustomerService                             │
└────────────────┬────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────┐
│        Data Access Layer (Repository)               │
│  Handles: Database queries, JPA operations          │
│  Interface: CustomerRepository                      │
└────────────────┬────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────┐
│          Data Model Layer (Entity)                  │
│  Handles: Object mapping, persistence              │
│  Class: Customer (JPA Entity)                       │
└────────────────┬────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────────┐
│            Database (MySQL)                         │
│  Table: customers                                   │
│  Columns: id, customerRegNo, name, email,          │
│           contactNumber, address                    │
└─────────────────────────────────────────────────────┘
```

## Database Schema

```sql
CREATE TABLE customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_reg_no VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    contact_number VARCHAR(20),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_customer_reg_no ON customers(customer_reg_no);
CREATE INDEX idx_email ON customers(email);
```

## File Descriptions

### Core Application Files

| File | Purpose |
|------|---------|
| `CustomerServiceApplication.java` | Main Spring Boot application entry point |
| `CustomerController.java` | REST API endpoints handler |
| `CustomerService.java` | Business logic implementation |
| `CustomerRepository.java` | Database query interface |
| `Customer.java` | JPA entity representing customer data |

### Configuration Files

| File | Purpose |
|------|---------|
| `SecurityConfig.java` | Spring Security & CORS configuration |
| `application.properties` | Production configuration |
| `application-dev.properties` | Development environment config |
| `pom.xml` | Maven dependencies & build config |

### Exception Handling

| File | Purpose |
|------|---------|
| `GlobalExceptionHandler.java` | Centralized exception handling |
| `ResourceNotFoundException.java` | Custom exception for not found errors |

### Testing Files

| File | Purpose |
|------|---------|
| `CustomerServiceTest.java` | Unit tests for business logic |
| `CustomerControllerTest.java` | Unit tests for REST endpoints |
| `CustomerServiceApplicationTests.java` | Integration tests |

### Build & Deployment

| File | Purpose |
|------|---------|
| `Dockerfile` | Container image definition |
| `docker-compose.yml` | Multi-container orchestration |
| `mvnw` | Maven wrapper (Unix/Linux) |
| `mvnw.cmd` | Maven wrapper (Windows) |

## Dependencies Overview

### Spring Boot Starters
- `spring-boot-starter-web` - Web and REST support
- `spring-boot-starter-data-jpa` - Database ORM
- `spring-boot-starter-security` - Authentication & Authorization
- `spring-boot-starter-validation` - Input validation
- `spring-boot-devtools` - Development tools

### Database
- `mysql-connector-j` - MySQL JDBC driver

### Utilities
- `lombok` - Reduce boilerplate code
- `spring-cloud-starter-openfeign` - Service communication

### Testing
- `spring-boot-starter-test` - Testing framework
- `mockito` - Mocking framework

## API Endpoints Summary

```
POST   /api/customers              - Create customer
GET    /api/customers              - Get all customers
GET    /api/customers/{id}         - Get customer by ID
GET    /api/customers/regno/{regNo} - Get customer by registration number
PUT    /api/customers/{id}         - Update customer
DELETE /api/customers/{id}         - Delete customer
```

## How to Build & Run

### Build
```bash
cd backend
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

### Docker
```bash
docker-compose up -d
```

### Test
```bash
mvn test
```

## Environment Configuration

See `.env.example` for required environment variables:
- `MYSQL_URL` - Database connection URL
- `MYSQL_USERNAME` - Database username
- `MYSQL_PASSWORD` - Database password
- `SERVER_PORT` - Application port
- `SPRING_ENVIRONMENT` - Environment (dev/prod)

---

**Project Version**: 1.0.0
**Java Version**: 21
**Spring Boot Version**: 3.4.3
**Build Tool**: Maven
**Database**: MySQL 8.0+
