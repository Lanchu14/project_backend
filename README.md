# 📊 PayTrack - Microservices Expense Management System

## 🚀 Overview

PayTrack is a **Microservices-based Expense Management System** built using **Spring Boot** and **Spring Cloud**.

This application allows users to manage expenses, categories, and generate reports using distributed microservices architecture.

---

# 🏗️ Architecture

This project follows **Microservices Architecture** with the following components:

* API Gateway
* Eureka Server (Service Discovery)
* Auth Service
* User Service
* Expense Service
* Category Service
* Report Service
* Notification Service

---

# 📦 Microservices List

| Service              | Description                         |
| -------------------- | ----------------------------------- |
| API Gateway          | Entry point for all client requests |
| Eureka Server        | Service Registry and Discovery      |
| Auth Service         | Authentication & Authorization      |
| User Service         | User Management                     |
| Expense Service      | Expense Tracking                    |
| Category Service     | Expense Categories                  |
| Report Service       | Expense Reports                     |
| Notification Service | Email / Notification Handling       |

---

# 🛠️ Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring Cloud
* REST API
* Maven

### Database

* MySQL / H2 Database

### Tools

* Git
* GitHub

---

# 🔧 Project Structure

```text
paytrack_backend
└── paytrack
    ├── api-gateway
    ├── auth-service
    ├── category-service
    ├── eureka-server
    ├── expense-service
    ├── notification-service
    ├── report-service
    └── user-service
```

---

# ⚙️ How to Run the Project

## Step 1 — Start Eureka Server

Run:

```bash
eureka-server
```

## Step 2 — Start Microservices

Run services in this order:

```bash
auth-service

user-service

category-service

expense-service

report-service

notification-service
```

## Step 3 — Start API Gateway

Run:

```bash
api-gateway
```

---

# 🌐 API Gateway

All requests go through:

```text
http://localhost:8080
```

---

# 🔍 Features

* User Registration & Login
* Expense Management
* Category Management
* Report Generation
* Microservices Architecture
* Service Discovery
* API Gateway Routing

---

# 🏗️ System Architecture

```text
Client
   ↓
API Gateway
   ↓
Microservices
   ↓
Database

Eureka Server
(Service Discovery)
```

---

# 👨‍💻 Author

**Lanchan J**

PayTrack Microservices Project

---

# ⭐ GitHub Repository

Repository:

https://github.com/Lanchu14/paytrack_backend

If you found this project useful, consider giving it a ⭐
