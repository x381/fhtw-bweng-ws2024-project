# HAKart: Backend Web Engineering Project

This repository contains the backend implementation for **HAKart**, an online marketplace. The project was created as part of the **Backend Web Engineering (BWENG)** course to demonstrate expertise in developing robust, secure, and scalable backend systems using modern development practices.

---

## 🚀 Motivation


**HAKart** serves as a Minimum Viable Product (MVP) designed for the **Clean Backend Competition**. It showcases:
- **RESTful API design** with proper HTTP methods and status codes.
- Seamless integration with a frontend from the Frontend Web Engineering course.
- **JWT-based authentication** and role-based access control.
- Secure, maintainable, and future-proof backend development using modern tools.

---

## 👥 Team Members

- **Hernán López Pérez**
- **Kyrylo Stepanets**
- **Arthur Zhu**

---

## 🛠️ Technologies Used

- **Java Spring Boot**: Framework for scalable REST API development.
- **Docker**: Simplified deployment with containerized applications.
- **MySQL (MariaDB)**: Relational database for reliable data persistence.
- **MinIO**: Object storage solution for file management (e.g., images).
- **Swagger**: Auto-generated API documentation and testing.
- **JWT (JSON Web Tokens)**: Secure user authentication and authorization.
- **JUnit**: Testing framework to ensure 80%+ code coverage.

---

## 📋 Features and Functional Requirements

### General Requirements:
- Adherence to RESTful principles with proper HTTP methods and status codes.
- Input validation and clear error messages.
- Use of JSON for data exchange (except for file uploads).
- Minimum of **80% test coverage** using unit and integration tests.

### Key Functionalities:

#### Public (Anonymous) Users:
- View homepage content, including the latest products and blog entries.

#### Registered Users:
- **Register** with:
    - Salutation
    - Valid email address
    - Username (min. 5 characters)
    - Password (min. 8 characters, including uppercase, lowercase, and numbers)
    - Country (ISO code)
    - Profile picture (or default placeholder if not provided)
- **Manage Content**:
    - Create, update, and delete products and blog entries.
    - Manage their own orders.
    - Edit personal data and passwords.

#### Admins:
- Oversee all platform data, including users, products, and orders.
- Full control to edit or delete user data and content.

#### Product Management:
- CRUD operations for products with file upload support for images.
- Filter and sort products by category, creation date, and creator.

#### Security:
- JWT-based authentication.
- Role-based access control for endpoints (users vs. admins).

---

## 🔄 Commit History and Key Features

### Highlights of Key Commits:
1. **Initial Setup**:
    - Spring Boot, Docker, and MySQL configuration.
    - Checkstyle setup for consistent coding standards.

2. **User Management**:
    - Create user registration and login with username or email.
    - Secure password updates and authentication with JWT tokens.
    - Add timestamps for user creation and modification.

3. **Product Management**:
    - Full CRUD operations for products.
    - File upload functionality for product images using MinIO.
    - File type validation and secure storage.

4. **Order Management**:
    - Develop `Order`, `OrderItem`, `OrderDto`, and related controllers and services.
    - Manage relationships between users and orders.

5. **File Handling**:
    - MinIO integration for handling image uploads and resources.

6. **Security Enhancements**:
    - Role-based access control for admin-only and user endpoints.
    - Improved exception handling and error messages.
    - CORS support for frontend integration.

7. **Testing**:
    - Achieve 80%+ code coverage with unit and integration tests.

8. **Swagger Integration**:
    - API documentation and testing available via Swagger UI.

---

## 🌐 API Documentation

The API documentation is auto-generated using **springdoc-openapi**:
- **JSON Format**: `/api`
- **Swagger UI**: `/swagger.html`

---

## 🛠️ Containerized Services

The backend system runs on Docker with the following containers:
1. **Spring Boot**: Backend service.
    - Port: `8080`
2. **MariaDB**: Relational database for persistent storage.
    - Port: `3306`
3. **MinIO**: Object storage for managing file uploads.
    - API Port: `9000`
    - Dashboard Port: `9001`

---

## 📊 API Endpoints

### Public Endpoints:
- `GET /api/home`: View the latest products or blog entries.
- `POST /api/register`: Register a new user.
- `POST /api/login`: Authenticate and receive a JWT token.

### User Endpoints:
- `GET /api/user/{id}`: View user profile.
- `PUT /api/user/password`: Update user password.

### Admin Endpoints:
- `GET /api/admin/users`: View all users.
- `DELETE /api/admin/user/{id}`: Delete a user.
- `GET /api/admin/data`: View all data entries.

### Product Endpoints:
- `POST /api/product`: Create a new product.
- `GET /api/products`: View all products.
- `GET /api/product/{id}`: Get details of a specific product.
- `PUT /api/product/{id}`: Update a product.
- `DELETE /api/product/{id}`: Delete a product.

---

## 🔄 Setup Instructions

### Prerequisites:
1. Install **Docker**: [Get Docker](https://docs.docker.com/get-docker/).
2. Install Java (17 or higher).

### Steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/x381/fhtw-bweng-ws2024-project
   cd fhtw-bweng-ws2024-project