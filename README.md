# 🛒 E-Commerce Backend Service

A robust, monolithic Spring Boot REST API engineered to serve as the backend engine for an e-commerce platform. It provides enterprise-grade stateless authentication, role-based authorization, catalog exploration, dynamic shopping cart state management, and transactional order checkout with relational integrity.

---

## 📌 Project Purpose

The primary purpose of this service is to manage the end-to-end commerce lifecycle for an online marketplace. In production e-commerce environments, applications must maintain strict consistency across catalog stock, active user carts, and checkout operations while enforcing zero-trust API security. This application addresses these requirements by:

1. **Securing Resources Statistically**: Implementing stateless JWT bearer token authentication and Role-Based Access Control (RBAC) across public endpoints, customer operations, and administrative dashboards.
2. **Preventing Race Conditions at Checkout**: Enforcing `@Transactional` boundaries during order placement to ensure cart item conversion, stock decrementing, and order creation occur atomically.
3. **Decoupling Data Representations**: Leveraging dedicated Data Transfer Objects (DTOs) and centralized global exception handling (`@RestControllerAdvice`) to prevent internal schema leakage and return standardized error payloads.

---

## 🚀 Key Features

- **Stateless RBAC Security**: Granular endpoint authorization separating customer capabilities (`ROLE_CUSTOMER`) from administrative control (`ROLE_ADMIN`) using Spring Security and HMAC-signed JWTs.
- **Transactional Order Placement**: Atomic checkout operations ensuring cart clearance and order record generation execute under strict database ACID guarantees.
- **Relational Integrity & Cascading**: Uses JPA foreign keys (`@ManyToOne`, `@OneToMany`) to manage user-to-cart, cart-to-item, and order-to-order-item lifecycles without orphan anomalies.
- **Centralized Validation & Exception Pipeline**: Custom exception hierarchies mapped to structured HTTP status codes (`400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `404 Not Found`, `409 Conflict`).
- **Flexible Database Profile**: Zero-infrastructure development via embedded H2 support, with turnkey compatibility for production MySQL instances.

---

## 🛠 Technologies Used

- **Java 17**
- **Spring Boot 3.x**
  - Spring Web MVC (REST APIs & Controller Layer)
  - Spring Security (Security Filter Chain & RBAC)
  - Spring Data JPA (Data Access Layer & Hibernate ORM)
  - Spring Boot Validation (Jakarta Bean Validation)
- **JSON Web Tokens (jjwt)** (Stateless Token Generation & Parsing)
- **MySQL / H2 Database** (Relational Persistence)
- **Lombok** (Boilerplate Reduction)
- **Maven** (Dependency & Build Lifecycle Management)

---

## 🏗 System Components

The application adheres to a clean monolithic layered architecture enforcing strict separation of concerns:

| Component | Responsibility |
| :--- | :--- |
| **SecurityFilterChain & JwtFilter** | Intercepts all incoming HTTP traffic, parses `Bearer <token>` headers, validates JWT signatures, and injects authentication tokens into the `SecurityContextHolder`. |
| **AuthController** | Exposes `/api/auth/register` and `/api/auth/login`, delegating credential validation to Spring Security's `AuthenticationManager`. |
| **ProductController & CategoryController** | Exposes public catalog browsing while restricting mutation endpoints (`POST`, `PUT`, `DELETE`) strictly to authenticated administrators. |
| **CartController & Service** | Manages user-specific shopping carts, item additions, quantity adjustments, and total price derivations. |
| **OrderController & Service** | Handles transactional checkout execution, order history retrieval, and administrative shipment/order status updates. |
| **GlobalExceptionHandler** | Intercepts framework and application-level exceptions, returning unified JSON response envelopes with distinct timestamps and error details. |
| **Relational Repositories** | Spring Data JPA interfaces abstracting database operations with indexed queries and transactional integrity. |

---

## 📐 Architecture Diagram

```mermaid
flowchart TB
    Client((External Client /\nFrontend / Postman))

    subgraph Spring Boot Application [E-Commerce Backend]
        subgraph Security Layer
            JwtFilter[JwtAuthenticationFilter]
            SecContext[(SecurityContextHolder)]
        end

        subgraph Controller Layer
            AuthCtrl[AuthController]
            ProdCtrl[ProductController]
            CartCtrl[CartController]
            OrderCtrl[OrderController]
        end

        subgraph Service Layer
            AuthSvc[AuthService]
            ProdSvc[ProductService]
            CartSvc[CartService]
            OrderSvc[OrderService]
        end

        subgraph Data Access Layer
            UserRepo[UserRepository]
            ProdRepo[ProductRepository]
            CartRepo[CartRepository]
            OrderRepo[OrderRepository]
        end
    end

    DB[(MySQL / H2 Database)]

    Client -- "HTTP Requests with Bearer JWT" --> JwtFilter
    JwtFilter -- "1. Validate Claims & Set Auth" --> SecContext
    JwtFilter -- "2. Dispatch Request" --> AuthCtrl & ProdCtrl & CartCtrl & OrderCtrl

    AuthCtrl --> AuthSvc
    ProdCtrl --> ProdSvc
    CartCtrl --> CartSvc
    OrderCtrl --> OrderSvc

    AuthSvc --> UserRepo
    ProdSvc --> ProdRepo
    CartSvc --> CartRepo
    OrderSvc --> OrderRepo

    UserRepo -.-> DB
    ProdRepo -.-> DB
    CartRepo -.-> DB
    OrderRepo -.-> DB

sequenceDiagram
    autonumber
    actor User as Client
    participant AuthCtrl as AuthController
    participant AuthMgr as AuthenticationManager
    participant JwtProv as JwtProvider
    participant DB as Relational Database

    User->>AuthCtrl: POST /api/auth/login {email, password}
    AuthCtrl->>AuthMgr: authenticate(UsernamePasswordAuthenticationToken)
    AuthMgr->>DB: Query User credentials by email
    DB-->>AuthMgr: UserEntity with hashed password & roles
    
    alt Invalid Credentials
        AuthMgr-->>AuthCtrl: BadCredentialsException
        AuthCtrl-->>User: 401 Unauthorized
    else Valid Credentials
        AuthMgr-->>AuthCtrl: Authenticated Principal
        AuthCtrl->>JwtProv: generateToken(userDetails)
        JwtProv-->>AuthCtrl: Signed JWT Token String
        AuthCtrl-->>User: 200 OK { token, type: "Bearer" }
    end
