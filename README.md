# WebBanDienThoai (Phone Shop E-commerce)

A full-stack e-commerce application for selling phones and accessories, built with Spring Boot backend and Angular frontend. The application consists of a customer-facing e-commerce platform and an admin dashboard for managing the store.

## Technology Stack

### Backend
- Java 23
- Spring Boot 3.4.3
- Spring Security with JWT
- Spring Data JPA/Hibernate
- MySQL 8.2
- Redis 7.2.3 (for caching)
- Maven

### Frontend (Customer & Admin)
- Angular 20
- Bootstrap 5.3
- NgBootstrap 19
- Font Awesome
- JWT Authentication
- Express.js (for SSR)

### DevOps & Tools
- Docker & Docker Compose
- PHPMyAdmin
- Redis Commander
- Git

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
- **Technology Stack**: Angular 19
- **Key Features**:
  - Responsive design
  - Product browsing and searching
  - Shopping cart functionality
  - User authentication
  - Order management

### 3. Admin Dashboard (webadmin)
- **Technology Stack**: Angular 19
- **Key Features**:
  - Product management
  - Order management
  - User management
  - Analytics dashboard

## Setup Instructions

## Development Setup

### Prerequisites
- Java 23
- Node.js (Latest LTS version)
- Docker & Docker Compose
- Maven
- Angular CLI

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

## Development Guidelines

For detailed information about code style, git workflow, testing guidelines, and best practices, please refer to [CONTRIBUTING.md](CONTRIBUTING.md).

### Key Principles
- Follow coding standards for both Java and TypeScript
- Write clean, maintainable, and well-documented code
- Include appropriate tests for new features
- Follow security best practices
- Use proper error handling and logging

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support, please open an issue in the GitHub repository or contact the development team.
   ```bash
   npm install
   ```

3. Run the development server:
   ```bash
   ng serve --port 4300
   ```
   Access the admin dashboard at http://localhost:4300
