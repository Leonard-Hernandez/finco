# Project Structure and Architecture

## Overview
This document outlines the project's structure and architecture, providing a clear understanding of the codebase organization.

## Project Structure

### Main Source Code (`src/main/java/com/finco/finco`)

#### Root Configuration
- AppConfig.java  # Usecase Bean Configuration
- Application.yml # Application configuration
- SecurityConfig.java # Security configuration
- OpenApiConfig.java # OpenApi configuration

#### 1. Domain Layer (`entity/`)
Core domain models exceptions gateways and contracts of the gateways

```
entity/
├── account/                   # Account management domain
│   ├── exception/             # Account-specific exceptions
│   ├── gateway/               # Account Gateway contracts
│   └── model/                 # Domain models and value objects
│
├── asset/                     # Asset management domain
│   ├── gateway/               # Asset Gateway contracts
│   └── model/                 # Domain models
│
├── goal/                      # Financial goals domain
│   ├── exception/             # Goal-specific exceptions
│   ├── gateway/               # Goal Gateway contracts
│   └── model/                 # Domain models
│
├── goalAccountBalance/        # Goal-account relationship
│   ├── gateway/               # GoalAccountBalance Gateway contracts
│   └── model/                 # Domain models
│
├── liabilitie/                # Liabilities domain
│   ├── gateway/               # Liabilities Gateway contracts
│   └── model/                 # Domain models
│
├── pagination/                # Pagination support
│
├── role/                      # Role-based access control
│   ├── exception/             # Role-specific exceptions
│   ├── gateway/               # Role Gateway contracts
│   └── model/                 # Role domain models
│
├── security/                  # Security domain
│   ├── exception/             # Security exceptions
│   ├── gateway/               # Security Gateway contracts
│
├── transaction/               # Financial transactions
│   ├── gateway/               # Transaction Gateway contracts
│   └── model/                 # Transaction domain models
│
└── user/                      # User management
│   ├── exception/             # User-related exceptions
│   ├── gateway/               # User Gateway contracts
│   └── model/                 # User domain models
├──security/
│   ├── exception/             # Security exceptions
│   └── gateway/               # Security Gateway contracts
```

#### 2. Use Case Layer (`usecase/`)
Application services implementing business use cases:

```
usecase/
├── account/     # Account management use cases(Business logic)
│   ├── dto/  # Account dto contracts
├── goal/        # Goal management use cases(Business logic)
│   └── dto/  # Goal dto contracts
├── transaction/ # Transaction processing use cases(Business logic)
│   └── dto/  # Transaction dto contracts
└── user/        # User management use cases(Business logic)
│   └── dto/  # User dto contracts
```

#### 3. Infrastructure Layer (`infrastructure/`)
Technical implementation details and framework-specific code:

```
infrastructure/
├── account/
│   ├── controller/    # Account REST endpoints
│   ├── dto/           # Account data transfer objects Implementations of dto contracts
│   ├── gateway/       # Account gateway implementations
│   └── validation/    # Account validation rules
│
├── goal/
│   ├── controller/    # Goal REST endpoints
│   ├── dto/           # Goal data transfer objects Implementations of dto contracts
│   ├── gateway/       # Goal gateway implementations
│   └── validation/    # Goal validation rules
│
├── transaction/
│   ├── controller/    # Transaction REST endpoints
│   ├── dto/           # Transaction data transfer objects Implementations of dto contracts
│   ├── gateway/       # Transaction gateway implementations
│   └── validation/    # Transaction validation rules
│
├── user/
│   ├── controller/    # User REST endpoints
│   ├── dto/           # User data transfer objects Implementations of dto contracts
│   ├── gateway/       # User gateway implementations
│   └── validation/    # User validation rules
│
├── config/            # Application configuration
│   ├── aop/          # Aspect-oriented programming
│   ├── db/           # Database configuration
│   │   ├── mapper/       # Mapper Entitys to Schema
│   │   ├── repository/     # Repositories
│   │   ├── schema/    # Schema of domain models
│   ├── error/        # Error handling
│   ├── pagination/   # Pagination configuration
│   ├── security/     # Security configuration
│   │   ├── controller/   # Validation dummy controller
│   │   ├── filter/   # security filters
│   │   ├── gateway/   # security gateway implementations of entities contracts
│   │   ├── service/   # security service implementations examples JwtService and UserDetailsService
│   └── springdoc/    # API documentation configuration
│
```

### Test Structure (`src/test/java/com/finco/finco`)
- Mirrors the main source structure
- Contains unit and integration tests
- Follows same package structure as main code

### Resources (`src/main/resources`)
```
resources/
├── application.properties  # Application configuration
├── db/                    # Database migrations
│   └── migration/         # Flyway migration scripts
└── context/               # Project documentation
    ├── structure.md       # This file
    ├── product.md         # Product documentation
    └── tech.md           # Technical documentation
```



## Architecture

The application follows a clean architecture with the following layers:

1. **Domain Layer** (`entity/`)
   - Contains all business logic and domain models
   - Defines core business rules and validations
   - Independent of frameworks and external concerns
   - Organized by bounded contexts (account, user, transaction, etc.)

2. **Application Layer** (`usecase/`)
   - Implements application use cases
   - Coordinates domain objects
   - Defines application-specific business rules
   - Organized by functional areas

3. **Infrastructure Layer** (`infrastructure/`)
   - Handles technical concerns
   - Implements interfaces defined in the domain layer
   - Includes web controllers, repositories, and external services
   - Organized by technical concerns and domain boundaries

## Architectural Patterns

- **Ports and Adapters**: Clear separation between application core and external concerns
- **Repository Pattern**: Abstract data access through interfaces
- **Dependency Injection**: For loose coupling and testability
- **CQRS**: Separate read and write models where appropriate
- **RESTful APIs**: For external communication

## Code Organization Principles

- **Feature-based organization**: Code is organized by feature/domain rather than by technical role
- **Package by feature**: Related classes are grouped together
- **Separation of concerns**: Clear boundaries between different responsibilities
- **Testability**: Design that facilitates unit and integration testing

## Naming Conventions

- **Packages**: Lowercase, singular (e.g., `account`, `user`)
- **Classes**: PascalCase (e.g., `AccountController`, `CreateAccountUseCase`)
- **Methods**: camelCase (e.g., `findById`, `createAccount`)
- **Variables**: camelCase (e.g., `accountId`, `userRepository`)
- **Constants**: UPPER_CASE (e.g., `MAX_RETRY_ATTEMPTS`)
