# 🛒 Enterprise E-Commerce RESTful Backend API

![Java](https://img.shields.io/badge/Java-17%2F21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Tokens-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

A foundational E-Commerce RESTful Backend engine built using **Java**, **Spring Boot**, **Spring Security**, **Spring Data JPA (Hibernate)**, and **MySQL**. 

This system handles user identity and Role-Based Access Control (RBAC) via stateless JWT tokens, basic product catalog management, and simple order placement.

---

## 📑 Table of Contents

- [Project Overview](#-project-overview)
- [Core Features](#-core-features)
- [System Architecture & Flow Diagrams](#-system-architecture--flow-diagrams)
- [Tech Stack](#-tech-stack)

---

## 🌟 Project Overview

This application serves as a backend engine for an e-commerce platform, focusing on standard RESTful principles and security:

- **Identity & Authorization:** Token-based stateless authentication using JWT.
- **Data Persistence:** Relational database mapping using JPA & Hibernate.
- **Clean Architecture:** Domain-Driven Layering (Controller → Service → Repository → Database).

---

## 🚀 Core Features

- **Authentication & RBAC:** User registration, password encryption via BCrypt, JWT generation/validation, and roles (`USER`, `ADMIN`).
- **Product Catalog Management:** Basic CRUD operations to add, view, and delete products (restricted to ADMIN for modifications).
- **Order Processing:** Checkout capabilities to convert requested product quantities into persistent order lines.

---

## 🏛️ System Architecture & Flow Diagrams

### High-Level Layered Architecture

```mermaid
graph TD
    Client["Client (Web / Mobile / Postman)"]
    
    subgraph SpringBootApp["Spring Boot Application Context"]
        subgraph SecurityLayer["Security & Filter Chain"]
            JWTFilter["JwtFilter"]
            AuthManager["AuthenticationManager / Provider"]
        end
        
        subgraph PresentationLayer["Controller / REST API Layer"]
            UserController["UserController"]
            ProductController["ProductController"]
            OrderController["OrderController"]
        end
        
        subgraph BusinessLayer["Service Layer (Business Logic)"]
            UserService["UserService"]
            ProductService["ProductService"]
            OrderService["OrderService"]
            CustomerUserDetailsService["CustomerUserDetailsService"]
        end

        subgraph PersistenceLayer["Data Access Layer (Spring Data JPA)"]
            UserRepo["UserRepository"]
            ProductRepo["ProductRepository"]
            OrderRepo["OrderRepository"]
        end
    end

    subgraph DatabaseEngine["Relational Storage"]
        MySQL[("MySQL Database Engine")]
    end

    Client -->|HTTP / REST Request| JWTFilter
    JWTFilter -->|Validate Token / Set SecurityContext| AuthManager
    JWTFilter --> PresentationLayer
    
    UserController --> UserService
    ProductController --> ProductService
    OrderController --> OrderService

    UserService --> UserRepo
    ProductService --> ProductRepo
    OrderService --> OrderRepo
    OrderService --> ProductRepo
    OrderService --> UserRepo

    UserRepo --> MySQL
    ProductRepo --> MySQL
    OrderRepo --> MySQL
```
