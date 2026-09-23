# 🛒 Enterprise E-Commerce RESTful Backend API

![Java](https://img.shields.io/badge/Java-17%2F21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Tokens-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)

A robust, enterprise-grade, scalable E-Commerce RESTful Backend engine built using **Java**, **Spring Boot 3**, **Spring Security 6**, **Spring Data JPA (Hibernate)**, and **MySQL**. 

This system handles user identity and Role-Based Access Control (RBAC) via stateless JWT tokens, catalog navigation, shopping cart sessions, transactional checkout/order lifecycles, and inventory consistency with strict ACID guarantees.

---

## 📑 Table of Contents

- [Project Overview](#-project-overview)
- [Core Features](#-core-features)
- [System Architecture & Flow Diagrams](#-system-architecture--flow-diagrams)
  - [1. High-Level Layered Architecture](#1-high-level-layered-architecture)
  - [2. JWT Authentication & Security Lifecycle](#2-jwt-authentication--security-lifecycle)
  - [3. Order Placement & Checkout Transaction Flow](#3-order-placement--checkout-transaction-flow)
  - [4. Database Entity-Relationship (ER) Diagram](#4-database-entity-relationship-er-diagram)
- [Detailed Architectural Topics & Flow Breakdown](#-detailed-architectural-topics--flow-breakdown)
  - [Layer-by-Layer Walkthrough](#layer-by-layer-walkthrough)
  - [Security & RBAC Enforcement](#security--rbac-enforcement)
  - [Transactional Integrity & Concurrency](#transactional-integrity--concurrency)
  - [Centralized Exception Handling & Validation](#centralized-exception-handling--validation)
- [Tech Stack](#-tech-stack)
- [API Endpoints Reference](#-api-endpoints-reference)
- [Project Directory Structure](#-project-directory-structure)
- [Getting Started & Local Setup](#-getting-started--local-setup)
- [Configuration](#-configuration)
- [Future Enhancements](#-future-enhancements)
- [License](#-license)

---

## 🌟 Project Overview

Modern e-commerce backends require strict separation of concerns, guaranteed database consistency, sub-second query execution, and high security. This application solves core e-commerce challenges:

- **Identity & Authorization:** Token-based stateless authentication preventing CSRF and enabling horizontal scalability.
- **Inventory Consistency:** Preventing race conditions, double checkouts, and overselling during simultaneous customer purchases.
- **Data Isolation:** Decoupling customer cart items from frozen order snapshots (pricing shifts do not alter historical orders).
- **Clean Architecture:** Domain-Driven Layering (Controller → Service → Repository → Database) ensuring testability, maintainability, and reusability.

---

## 🚀 Core Features

- **Authentication & RBAC:** User registration, password encryption via BCrypt, JWT generation/validation, and roles (`ROLE_CUSTOMER`, `ROLE_ADMIN`).
- **Product Catalog Management:** Category hierarchical grouping, multi-parameter search, pagination, sorting (by price, rating, date), and stock monitoring.
- **Cart Management:** Dynamic addition, quantity update, stock pre-validation, and cart-clearing mechanisms tied to authenticated user sessions.
- **Order Processing Engine:** Atomic checkout converting cart items into persistent order line snapshots with status tracking (`PENDING`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED`).
- **Address & Profile Management:** Shipping/billing address management linked to user accounts.
- **Robust Exception Handling:** Uniform API error response model with standard HTTP error codes, timestamping, and actionable validation messages.

---

## 🏛️ System Architecture & Flow Diagrams

### 1. High-Level Layered Architecture

The following diagram illustrates how an incoming HTTP request travels through the system layers:

```mermaid
graph TD
    Client["Client (Web / Mobile / Postman)"]
    
    subgraph SpringBootApp["Spring Boot Application Context"]
        subgraph SecurityLayer["Security & Filter Chain"]
            CORS["CORS & CSRF Filter"]
            JWTFilter["JwtAuthenticationFilter"]
            AuthManager["AuthenticationManager / Provider"]
        end
        
        subgraph PresentationLayer["Controller / REST API Layer"]
            AuthController["AuthController"]
            ProductController["ProductController"]
            CartController["CartController"]
            OrderController["OrderController"]
            GlobalExceptionHandler["@RestControllerAdvice (Exception Handler)"]
        end
        
        subgraph BusinessLayer["Service Layer (Business Logic)"]
            AuthService["AuthService"]
            ProductService["ProductService"]
            CartService["CartService"]
            OrderService["OrderService (@Transactional)"]
        end

        subgraph PersistenceLayer["Data Access Layer (Spring Data JPA)"]
            UserRepo["UserRepository"]
            ProductRepo["ProductRepository"]
            CartRepo["CartRepository"]
            OrderRepo["OrderRepository"]
        end
    end

    subgraph DatabaseEngine["Relational Storage"]
        MySQL[("MySQL Database Engine")]
    end

    Client -->|HTTP / REST Request| CORS
    CORS --> JWTFilter
    JWTFilter -->|Validate Token / Set SecurityContext| AuthManager
    JWTFilter --> PresentationLayer
    
    AuthController --> AuthService
    ProductController --> ProductService
    CartController --> CartService
    OrderController --> OrderService

    AuthService --> UserRepo
    ProductService --> ProductRepo
    CartService --> CartRepo
    OrderService --> OrderRepo
    OrderService --> ProductRepo

    UserRepo --> MySQL
    ProductRepo --> MySQL
    CartRepo --> MySQL
    OrderRepo --> MySQL

    GlobalExceptionHandler -.->|Intercepts & Normalizes Errors| Client
