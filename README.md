# HAKart: Backend Web Engineering Project

This repository contains the backend implementation for **HAKart**, an online marketplace. The project was created as part of the **Backend Web Engineering (BWENG)** course to demonstrate expertise in developing robust, secure, and scalable backend systems using modern development practices.

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

## 🌐 API Documentation

The API documentation is auto-generated using **springdoc-openapi**:
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html#/`

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

## 🔄 Setup Instructions

### Prerequisites:
1. Install **Docker**: [Get Docker](https://docs.docker.com/get-docker/).
2. Install Java (17 or higher).

### Steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/x381/fhtw-bweng-ws2024-project
   cd fhtw-bweng-ws2024-project