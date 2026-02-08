# Finco - Personal Finance Management API
try on https://api.fincoai.app/finco-api/v1/swagger-ui.html

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

2. **Configure the env Variables**
   - Create a new MySQL database
   - Update `.env` with your database credentials, OpenAi key adn front-end url

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
The application will be available at `http://localhost:8080`

## 🐳 Docker Setup (Recommended)

The easiest way to run Finco is using Docker Compose, which will automatically set up both MySQL and the application.

### Prerequisites

- Docker and Docker Compose installed
- Git

### Quick Start with Docker Compose

1. **Clone the repository**
   ```bash
   git clone https://github.com/Leonard-Hernandez/finco.git
   cd finco
   ```

2. **Configure environment variables**
   Create a `.env` file in the root directory with your configuration:
   
   ```plaintext
   # Database connection
   DbUrl=mysql:3306
   UserDb=finco_user
   password=finco_password
   
   # Application settings
   FRONTEND_URL=http://localhost:4200
   openai-key=your_openai_key
   ```

3. **Update Docker Compose credentials** (if needed)
   If you want to use different database credentials than the defaults, update the `docker-compose.yml` file:
   
   ```yaml
   services:
     mysql:
       environment:
         MYSQL_ROOT_PASSWORD: your_root_password
         MYSQL_DATABASE: Finco
   ```

   **Important**: Make sure the credentials in `.env` and `docker-compose.yml` match:
   - `.env` → `password` should match `MYSQL_ROOT_PASSWORD` in docker-compose
   - `.env` → `DbUrl` should be `mysql:3306` (container name)

4. **Build and run with Docker Compose**
   ```bash
   docker-compose up -d
   ```

5. **Access the application**
   - **API**: `http://localhost:8086`
   - **Swagger UI**: `http://localhost:8086/finco-api/v1/swagger-ui.html`

### Docker Compose Commands

- **Start services**: `docker-compose up -d`
- **Stop services**: `docker-compose down`
- **View logs**: `docker-compose logs -f`
- **Rebuild application**: `docker-compose up --build -d`

## 🐳 Manual Docker Setup

If you prefer to run Docker containers manually:

1. **Create `.env` file** (same configuration as above)

2. **Build the container image**
   ```bash
   docker build -t finco .
   ```

3. **Run the container**
   ```bash
   docker run -p 8086:8086 -d --name finco --env-file .env finco
   ```

## 📚 API Documentation

Once the application is running, you can access:

- **Swagger UI**: `http://localhost:8086/finco-api/v1/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8086/finco-api/v1/v3/api-docs`

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
