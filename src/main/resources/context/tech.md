# Technical Stack and Dependencies

## Core Technologies

### Backend
- **Java 17** - Core programming language
- **Spring Boot 3.5.4** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data access layer
- **Hibernate** - ORM framework
- **Flyway** - Database migration tool
- **MySQL** - Primary database
- **JWT (JSON Web Tokens)** - For authentication
- **SpringDoc OpenAPI** - API documentation
- **Lombok** - Code generation for boilerplate code
- **Spring Boot DevTools** - Developer tools

## Dependencies

### Core Dependencies
- **Spring Boot**: 3.5.4
- **Spring Boot Web**: 3.5.4
- **Spring Data JPA**: 3.5.4
- **Spring Security**: 3.5.4
- **Spring Validation**: 3.5.4
- **Flyway Core**: 3.5.4
- **Flyway MySQL**: 3.5.4
- **MySQL Connector/J**: 8.0.33
- **JJWT API**: 0.12.6
- **JJWT Impl**: 0.12.6
- **JJWT Jackson**: 0.12.6
- **SpringDoc OpenAPI**: 2.8.9

### Development Dependencies
- **Spring Boot DevTools**: 3.5.4
- **Lombok**: (inherited from Spring Boot)

### Test Dependencies
- **Spring Boot Test**: 3.5.4

## Build Tools
- **Maven** - Dependency management and build automation
- **Java 17** - Target JDK

## Development Tools
- **Spring Boot DevTools** - Automatic restart, live reload
- **Lombok** - Reduces boilerplate code

## API Documentation
- **SpringDoc OpenAPI** - Interactive API documentation
  - Accessible at: `/swagger-ui.html`
  - OpenAPI JSON: `/v3/api-docs`

## Security
- **JWT-based authentication**
- **Spring Security** for authorization
- **Password hashing** with BCrypt

## Database
- **MySQL** for production
- **Flyway** for database migrations