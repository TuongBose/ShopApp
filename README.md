# WebBanDienThoai (Phone Shop E-commerce)

A full-stack e-commerce application for selling phones and accessories, built with Spring Boot backend and Angular frontend.

## Project Structure

The project is divided into three main components:

### 1. Backend (ShopApp_Backend)
- **Technology Stack**: Spring Boot 3.4, Spring Security, Spring MVC, JPA/Hibernate
- **Database**: MySQL
- **Key Features**:
  - RESTful API endpoints
  - JWT Authentication
  - File upload functionality
  - Data validation
  - Database relationships

Structure:
```
ShopApp_Backend/
├── Shopapp/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/         # Java source files
│   │   │   └── resources/    # Application properties, static resources
│   │   └── test/            # Test files
│   ├── pom.xml              # Maven dependencies
│   └── uploads/             # File upload directory
└── DataBase.sql            # Database schema
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

### Prerequisites
- Java 23
- Node.js (Latest LTS version)
- MySQL
- Maven
- Angular CLI

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd ShopApp_Backend/Shopapp
   ```

2. Configure database:
   - Create a MySQL database
   - Import the schema from `DataBase.sql`
   - Update `application.properties` with your database credentials

3. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
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
   ng serve
   ```
   Access the application at http://localhost:4200

### Admin Dashboard Setup
1. Navigate to the admin directory:
   ```bash
   cd webadmin
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Run the development server:
   ```bash
   ng serve --port 4300
   ```
   Access the admin dashboard at http://localhost:4300
