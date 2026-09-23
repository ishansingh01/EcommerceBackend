# 🛒 E-Commerce RESTful Backend API

A high-performance, robust, and scalable e-commerce backend built with **Java**, **Spring Boot**, **Spring Security with JWT**, and **Spring Data JPA**. This service powers core online retail functionality including user authentication, product catalog browsing, cart operations, order processing, and administrative inventory management.

---

## 📑 Table of Contents
1. [Key Features](#-key-features)
2. [Tech Stack](#-tech-stack)
3. [System Architecture & Request Flow](#-system-architecture--request-flow)
4. [Database Design & Entity Relationships](#-database-design--entity-relationships)
5. [Project Structure](#-project-structure)
6. [API Endpoints Overview](#-api-endpoints-overview)
7. [Getting Started & Local Setup](#-getting-started--local-setup)
8. [Configuration (`application.properties`)](#-configuration-applicationproperties)
9. [Future Enhancements](#-future-enhancements)

---

## ✨ Key Features

- **Authentication & Authorization**: Stateless JWT (JSON Web Token) authentication with role-based access control (`ROLE_CUSTOMER`, `ROLE_ADMIN`).
- **Product & Category Management**: Dynamic catalog searching, category filtering, pagination, and sorting.
- **Shopping Cart Lifecycle**: Add, update quantity, and remove items with real-time price tallying.
- **Order & Checkout Processing**: Cart-to-order transition with stock validation and transaction consistency.
- **Data Validation & Error Handling**: Global exception handling (`@RestControllerAdvice`) delivering uniform error envelopes.
- **Relational Integrity**: Complete JPA/Hibernate entity relationships with transactional boundaries (`@Transactional`).

---

## 🛠 Tech Stack

- **Language:** Java 17+
- **Framework:** Spring Boot 3.x
  - **Spring Web** (RESTful API development)
  - **Spring Security** (Security filter chains & authorization)
  - **Spring Data JPA** (Data persistence with Hibernate ORM)
- **Token Management:** `jjwt` (Java JWT library)
- **Database:** MySQL 8 / PostgreSQL (H2 Database for local integration testing)
- **Build Tool:** Apache Maven
- **Utilities:** Lombok, ModelMapper / MapStruct

---

## 🏗 System Architecture & Request Flow

The system is designed following the standard **Controller-Service-Repository** layered pattern with an intercepted security filter chain.

### Layered Architecture Flow
