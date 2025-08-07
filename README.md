# ShopApp E-commerce Project

## ⚠️ Important Notice

**DO NOT USE THIS CODE IN ANY FORM WITHOUT PERMISSION**
This project is proprietary and protected by copyright. Any unauthorized use, reproduction, or distribution is strictly prohibited.

## 📱 Overview

A full-stack e-commerce application for selling phones and accessories, built with Spring Boot backend and Angular frontend. The application consists of a customer-facing e-commerce platform and an admin dashboard for managing the store.

## Technology Stack

### Backend
- Java 17
- Spring Boot
- Spring Security with JWT
- Spring Data JPA/Hibernate
- MySQL 8.2.0
- Redis 7.2.3 (for caching)
- Apache Kafka (Message Broker)
- OAuth2 Social Login
- Maven 3.9.x

### Frontend (Customer & Admin)
- Angular 16+
- TypeScript 5.x
- Bootstrap 5.3
- Angular Material
- RxJS
- JWT Authentication
- OAuth2 Integration

### DevOps & Infrastructure
- Docker & Docker Compose
- PHPMyAdmin
- Redis
- Apache Kafka
- Nginx (Production)
- GitHub CI/CD

## Project Structure

The project is organized into several key components:

### 1. Backend (ShopApp_Backend)
- **Technology Stack**: Spring Boot 3.4, Spring Security, Spring MVC, JPA/Hibernate
- **Database**: MySQL
- **Key Features**:
  - RESTful API endpoints
  - JWT Authentication
  - File upload functionality
  - Data validation
  - Database relationships

### Project Organization
```
ShopApp/
├── ShopApp_Backend/        # Spring Boot Backend
│   ├── DataBase.sql        # Database schema
│   └── Shopapp/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   │   ├── controllers/    # REST API endpoints
│       │   │   │   ├── dtos/           # Data Transfer Objects
│       │   │   │   ├── models/         # Domain entities
│       │   │   │   ├── repositories/   # Data access layer
│       │   │   │   └── services/       # Business logic
│       │   │   └── resources/          # Configuration files
│       │   └── test/                   # Unit & integration tests
│       ├── pom.xml                     # Maven dependencies
│       └── uploads/                    # File storage
│
├── ShopApp_Frontend/       # Angular Customer Frontend
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/   # Reusable UI components
│   │   │   ├── services/     # API services
│   │   │   ├── models/       # Data models
│   │   │   └── pages/        # Route components
│   │   └── assets/           # Static resources
│   └── package.json
│
├── webadmin/               # Angular Admin Dashboard
│   └── src/
│       └── app/
│           ├── components/    # Admin UI components
│           ├── services/      # Admin API services
│           └── pages/         # Admin pages
│
├── shopapp-docker/         # Docker configuration
│   ├── docker-compose.yaml # Infrastructure services
│   └── redis-data/        # Redis persistence
│
└── kafka-docker/          # Kafka configuration (if used)
    └── docker-compose.yaml
```

### 2. Customer Frontend (ShopApp_Frontend)
- **Technology Stack**: Angular 16+
- **Key Features**:
  - Responsive design
  - Product browsing and searching
  - Shopping cart functionality
  - User authentication & OAuth2
  - Order management
  - Real-time updates

### 3. Admin Dashboard (webadmin)
- **Technology Stack**: Angular 16+
- **Key Features**:
  - Product management
  - Order management
  - User management
  - Analytics dashboard

## Setup Instructions

## Development Setup

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- Docker & Docker Compose
- Maven 3.9.x
- Angular CLI
- MySQL 8.2.0
- Redis 7.2.3
- Apache Kafka
- Git

### Using Docker (Recommended)
1. Clone the repository:
   ```bash
   git clone https://github.com/TuongBose/ShopApp.git
   cd ShopApp
   ```

2. Start the infrastructure services:
   ```bash
   cd shopapp-docker
   docker-compose up -d mysql-container redis-container phpmyadmin-container
   ```
   - MySQL will be available on port 3306
   - Redis on port 6379
   - PHPMyAdmin on port (check docker-compose.yaml)

3. Import database schema:
   ```bash
   mysql -h localhost -u root -p < ShopApp_Backend/DataBase.sql
   ```

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd ShopApp_Backend/Shopapp
   ```

2. Configure application:
   - Copy `src/main/resources/application.properties.example` to `application.properties`
   - Update database credentials and Redis configuration

3. Build and run:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
   The server will start on http://localhost:8080

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd ShopApp_Frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Run the development server:
   ```bash
   npm start
   ```
   Access the application at http://localhost:4200

### Admin Dashboard Setup
1. Navigate to the admin directory:
   ```bash
   cd webadmin
   ```

2. Install dependencies and run:
   ```bash
   npm install
   npm start
   ```
   Access the admin dashboard at http://localhost:4300

## Production Deployment
1. Uncomment the `shopapp-spring-container` and `shopapp-angular-container` services in `shopapp-docker/docker-compose.yaml`
2. Build and start all containers:
   ```bash
   docker-compose up -d --build
   ```

## 💻 Development Guidelines

### Code Principles
- Follow coding standards for both Java and TypeScript
- Write clean, maintainable, and well-documented code
- Include appropriate tests for new features
- Follow security best practices
- Use proper error handling and logging

### Version Control
- Create feature branches from main
- Use meaningful commit messages
- Review code before merging
- Keep commits focused and atomic

### Security Guidelines
- Never commit sensitive data
- Use environment variables for configurations
- Implement proper authentication
- Follow OWASP security practices

## 🚫 Usage Restrictions

This software is protected under copyright law. Please refer to the COPYRIGHT file in the root directory for complete terms and conditions.

For quick reference, the following actions are strictly prohibited:
- Commercial use
- Distribution
- Modification
- Patent use
- Private use without permission

## 📞 Support

For authorized support inquiries only, please contact the development team directly.
Do not create public issues or fork this repository without explicit permission.
