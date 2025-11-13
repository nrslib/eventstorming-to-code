# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Event Storming Sample Codes is a Spring Boot application demonstrating Domain-Driven Design (DDD) patterns inspired by Event Storming methodology. The project contains two main bounded contexts:
- **SNS Module**: Document management with user interactions (effectiveness tracking)
- **Payment Module**: Payment contract management with overdue detection batch processing

## Technology Stack

- **Language**: Java 23
- **Framework**: Spring Boot 3.4.2
- **Build Tool**: Gradle
- **Database**: H2 (in-memory) for development, MySQL for production
- **ORM**: Spring Data JPA with Hibernate
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Utilities**: Lombok for reducing boilerplate

## Build and Development Commands

### Build the Project
```bash
./gradlew build
```

### Run the Application
```bash
./gradlew bootRun
```

### Run Tests
```bash
./gradlew test
```

### Run a Single Test Class
```bash
./gradlew test --tests "ClassName"
```

### Run a Single Test Method
```bash
./gradlew test --tests "ClassName.methodName"
```

### Clean Build
```bash
./gradlew clean build
```

### Access API Documentation
When the application is running, Swagger UI is available at:
```
http://localhost:8080/swagger-ui.html
```

### Access H2 Database Console
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:test
Username: sa
Password: (empty)
```

## Architecture and Code Organization

### Modular Structure

The codebase follows a **modular monolith** pattern with bounded contexts as modules under `modules/`:

```
src/main/java/org/example/eventstormingsamplecodes/
├── modules/
│   ├── sns/           # SNS bounded context
│   └── payment/       # Payment bounded context
├── http/controllers/  # Shared HTTP infrastructure (error handling)
└── config/            # Application-wide configuration
```

### Layer Architecture per Module

Each module follows **DDD layered architecture**:

```
modules/{module-name}/
├── app/
│   ├── domain/models/           # Domain entities and repositories (interfaces)
│   ├── application/services/    # Application services (use cases)
│   └── infrastucture/
│       └── persistence/
│           ├── datamodel/       # JPA entities (infrastructure concern)
│           ├── jpa/             # Spring Data JPA repositories
│           ├── mapper/          # Conversion between domain and data models
│           └── impl/            # Repository interface implementations
└── http/
    ├── controllers/             # REST API controllers
    └── models/                  # Request/Response DTOs
```

### Key Architectural Patterns

**1. Domain Model Separation**
- Domain models (e.g., `Document`) are pure Java objects with business logic
- JPA entities (e.g., `DocumentDataModel`) are separate infrastructure concerns
- Mappers handle conversion between domain and data models
- This keeps domain logic independent of persistence framework

**2. Repository Pattern**
- Domain defines repository interfaces (e.g., `DocumentRepository`)
- Infrastructure provides implementations (e.g., `DocumentRepositoryImpl`)
- Controllers should NOT directly use JPA repositories; use domain repositories instead
  - Exception: Read-only queries in controllers may use JPA repositories for performance

**3. Application Services**
- Orchestrate domain operations and repository calls
- Handle cross-cutting concerns like transaction boundaries
- Examples: `DocumentApplicationService`, `PaymentOverdueNotificationService`

**4. Value Objects**
- Use type-safe wrappers for primitive types (e.g., `DocumentId`, `UserId`)
- Provides compile-time safety and domain clarity

**5. Custom Bean Naming**
- Uses `FullyQualifiedBeanNameGenerator` to avoid bean name conflicts in modular architecture
- Allows multiple modules to have classes with the same simple name

### SNS Module Specifics

**Domain Model**: `Document`
- Tracks document content, creator, and modification history
- Supports "effectiveness" tracking: users can mark documents as effective for them
- Stored in separate tables: `documents` (main) and `document_effectives` (many-to-many with users)

**Key Operations**:
- Create/Update documents
- Mark/Unmark document effectiveness per user
- Query documents by effectiveness

### Payment Module Specifics

**Batch Processing Pattern**:
- `OverduePaymentDetectionBatch` demonstrates chunk-based batch processing
- Processes contracts in configurable chunks (default: 100)
- Handles failures gracefully with per-contract error handling
- Updates contract status and triggers notifications

**Key Components**:
- `ContractDataModel`: Represents payment contracts with status tracking
- `PaymentOverdueNotificationService`: Sends notifications for overdue payments
- Template-based notification system with `NotificationTemplateDataModel`

## Development Guidelines

### Adding a New Module

1. Create module structure under `modules/{module-name}/`
2. Follow the layered architecture: domain → application → infrastructure → http
3. Define domain models with business logic
4. Create separate JPA entities for persistence
5. Implement repository pattern with domain interfaces and infrastructure implementations
6. Expose functionality through application services
7. Add REST controllers in the http layer

### Database Configuration

Development uses H2 in-memory database (auto-configured).

For MySQL in production, update `application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dbname
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: your_username
    password: your_password
```

### Testing Strategy

- Unit tests for domain models and application services
- Integration tests for batch processing (see `OverduePaymentDetectionBatchTest`)
- Use `@SpringBootTest` for full application context tests
- Use `@DataJpaTest` for repository tests