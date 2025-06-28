# FinCo - Personal Finance Management API

FinCo is a robust, scalable, and clean-architecture based API designed to help you manage your personal finances in an agile and efficient way. This project serves as both a practical financial tool and a learning resource for implementing clean architecture principles in a real-world application.

## 🚀 Features

- **Clean Architecture Implementation**: Following SOLID principles and clean architecture patterns
- **Agile Financial Management**: Track income, expenses, and investments with ease
- **Scalable Design**: Built with scalability and maintainability in mind
- **RESTful API**: Standardized endpoints for seamless integration

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/finco/
│   │   ├── entity/         # Domain entities and business logic
│   │   │   ├── account/    # Account management
│   │   │   ├── asset/      # Asset management
│   │   │   ├── goal/       # Financial goals
│   │   │   ├── role/       # User roles and permissions
│   │   │   ├── user/       # User management
│   │   │   └── transaction/# Transaction handling
│   │   ├── infrastructure/ # Infrastructure layer
│   │   │   ├── config/     # Application configuration
│   │   │   └── [module]/   # Module-specific infrastructure
│   │   └── usecase/        # Application use cases
│   └── resources/          # Configuration files
└── test/                   # Test suite
```

## 🛠️ Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- MySQL 8.0 or higher (or any compatible SQL database)

## 🚀 Getting Started

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/finco.git
   cd finco
   ```

2. **Configure the database**
   - Create a new MySQL database
   - Update `application.properties` with your database credentials

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`


## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

Please make sure to update tests as appropriate and follow our code of conduct.


## 📊 Database Schema

![Database Schema](https://github.com/user-attachments/assets/c4ab39c3-2cc6-4062-b69c-95451b7dc06f)

