# Finco - Personal Finance Management API

Finco is a robust, scalable, and clean-architecture based API designed to help you manage your personal finances with support for multiple currencies. Built with Spring Boot and following clean architecture principles, Finco provides a solid foundation for personal financial management.

## 🚀 Key Features

- **Multi-Currency Support**: Manage accounts in different currencies with automatic exchange rate handling
- **Smart Transfers**: Transfer funds between accounts with automatic currency conversion
- **Transparent Fee System**: Detailed tracking of all transaction fees and exchange rates
- **Savings Goals**: Set and track financial goals with support for multiple currency funds
- **Clean Architecture**: Well-structured codebase following SOLID principles
- **RESTful API**: Standardized endpoints for easy integration
- **JWT Authentication**: Secure access to all endpoints
- **API Documentation**: Interactive documentation with Swagger UI

## 🏗️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.5.4
- **Security**: Spring Security, JWT
- **Database**: MySQL 8.0+, H2 (for testing)
- **API Documentation**: SpringDoc OpenAPI 2.8.9
- **Build Tool**: Maven 3.6+
- **Other**: Lombok, Flyway, JPA/Hibernate

## 🛠️ Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- MySQL 8.0 or higher
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Leonard-Hernandez/finco.git
   cd finco
   ```

2. **Configure the database**
   - Create a new MySQL database
   - Update `src/main/resources/application.yml` with your database credentials

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8080`

## 📚 API Documentation

Once the application is running, you can access:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🏗️ Project Structure

```
com.finco.finco/
├── entity/               # Domain entities and business logic
│   ├── account/         # Account management
│   ├── asset/           # Asset management
│   ├── goal/            # Financial goals
│   ├── role/            # User roles and permissions
│   ├── user/            # User management
│   └── transaction/     # Transaction handling
├── usecase/             # Application use cases
│   ├── account/         # Account operations
│   ├── goal/            # Goal operations
│   ├── transaction/     # Transaction operations
│   └── user/            # User operations
└── infrastructure/      # Framework and infrastructure
    ├── config/          # Application configuration
    ├── controller/      # REST controllers
    ├── dto/             # Data Transfer Objects
    ├── gateway/         # Interface implementations
    └── validation/      # Validation rules
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📧 Contact

For any questions or feedback, please open an issue or contact the maintainers.
