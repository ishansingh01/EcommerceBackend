# 🛒 E-Commerce REST API Backend

A production-ready, modular E-Commerce RESTful Backend built with **Java 17**, **Spring Boot 3**, **Spring Security**, **JWT (JSON Web Tokens)**, and **MySQL/JPA**. 

This system provides enterprise-grade user authentication, catalog management, shopping cart operations, and order processing with role-based access control (RBAC).

---

## 📑 Table of Contents
1. [Key Features](#-key-features)
2. [Tech Stack](#-tech-stack)
3. [System Architecture](#-system-architecture)
4. [Request & Authentication Flow](#-request--authentication-flow)
5. [Database Entity-Relationship (ER) Overview](#-database-entity-relationship-er-overview)
6. [API Endpoints Reference](#-api-endpoints-reference)
7. [Project Directory Structure](#-project-directory-structure)
8. [Getting Started & Local Setup](#-getting-started--local-setup)
9. [Configuration](#-configuration)
10. [Future Enhancements](#-future-enhancements)

---

## 🚀 Key Features

* **Stateless Authentication & Security**: Complete role-based access control (RBAC: `ROLE_CUSTOMER`, `ROLE_ADMIN`) powered by Spring Security and HMAC-signed JWT tokens.
* **Product Catalog**: Full CRUD lifecycle for categories and products with inventory tracking and pagination/filtering support.
* **Shopping Cart Engine**: Persistent shopping cart per user supporting dynamic quantity updates, pricing totals, and automatic cleanup.
* **Order & Checkout Processing**: Atomic order creation from cart items with status management (`PENDING`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED`).
* **Validation & Error Handling**: Centralized global exception handler (`@RestControllerAdvice`) returning standardized JSON error payloads.
* **Database Management**: Hibernate / Spring Data JPA with transactional boundaries (`@Transactional`) ensuring data consistency.

---

## 🛠 Tech Stack

| Technology | Purpose |
| :--- | :--- |
| **Java 17+** | Core Programming Language |
| **Spring Boot 3.x** | Application Framework & Dependency Injection |
| **Spring Security** | Authentication, Authorization & Security Filters |
| **JSON Web Tokens (jjwt)** | Stateless Bearer Token Authentication |
| **Spring Data JPA (Hibernate)** | Object-Relational Mapping (ORM) & Database Abstraction |
| **MySQL** | Production Relational Database |
| **Lombok** | Boilerplate Reduction (Getters, Setters, Builders) |
| **Maven** | Build Automation and Dependency Management |

---

## 🏛 System Architecture

The project follows the standard **Layered Architecture (N-Tier)** design pattern to enforce separation of concerns, loose coupling, and maintainability:

```text
                  +-----------------------------------+
                  |      Client / Frontend / Postman  |
                  +-----------------+-----------------+
                                    |
                             HTTP / HTTPS
                                    |
                  +-----------------v-----------------+
                  |       Servlet Filter Chain        |
                  |  [JwtAuthenticationFilter / CORS] |
                  +-----------------+-----------------+
                                    |
+-----------------------------------v-------------------------------------+
| SPRING BOOT CONTAINER                                                   |
|                                                                         |
|  +-------------------------------------------------------------------+  |
|  |                     Controller Layer (REST APIs)                  |  |
|  |   AuthController  |  ProductController  |  Cart/OrderController   |  |
|  +---------------------------------+---------------------------------+  |
|                                    |                                    |
|                                    v                                    |
|  +-------------------------------------------------------------------+  |
|  |                   Service Layer (Business Logic)                  |  |
|  |   AuthService     |  ProductService     |  Cart/OrderService      |  |
|  +---------------------------------+---------------------------------+  |
|                                    |                                    |
|                                    v                                    |
|  +-------------------------------------------------------------------+  |
|  |               Repository Layer (Spring Data JPA / DAOs)           |  |
|  |   UserRepository  |  ProductRepository  |  OrderRepository        |  |
|  +---------------------------------+---------------------------------+  |
+------------------------------------|------------------------------------+
                                     |
                             JDBC / Hibernate
                                     |
                  +------------------v----------------+
                  |         MySQL Database            |
                  +-----------------------------------+
