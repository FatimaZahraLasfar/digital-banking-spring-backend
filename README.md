# E-Banking Backend — Secured with Spring Security & JWT

> Spring Boot REST API for a Digital Banking application, secured with Spring Security and JSON Web Tokens (JWT).

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Security Architecture](#security-architecture)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Authentication Flow](#authentication-flow)
- [Demo Users](#demo-users)
- [Commit History](#commit-history)

---

## Overview

This is the secured backend of a Digital Banking use case built with Spring Boot. It exposes a RESTful API consumed by an Angular frontend. The security layer uses **stateless JWT authentication** — no sessions, no cookies. Every protected request must carry a valid `Authorization: Bearer <token>` header.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.x |
| Security | Spring Security 6 + JJWT 0.11.5 |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL (MariaDB dialect) |
| Mapping | BeanUtils (manual mapper) |
| Boilerplate | Lombok |
| API Docs | SpringDoc OpenAPI (Swagger UI) |

---

## Project Structure

```
src/main/java/ma/fzl/ebankingbackend/
│
├── security/
│   ├── SecurityConfig.java           # Main security configuration
│   ├── JwtUtils.java                 # Token generation, parsing, validation
│   └── JwtAuthenticationFilter.java  # OncePerRequestFilter — validates Bearer token
│
├── web/
│   ├── AuthController.java           # POST /auth/login
│   ├── BankAccountRestController.java
│   └── CustomerRestController.java
│
├── dtos/
│   ├── LoginRequestDTO.java
│   ├── LoginResponseDTO.java
│   ├── CustomerDTO.java
│   ├── BankAccountDTO.java
│   ├── CurrentBankAccountDTO.java
│   ├── SavingBankAccountDTO.java
│   ├── AccountOperationDTO.java
│   ├── AccountHistoryDTO.java
│   ├── DebitDTO.java
│   ├── CreditDTO.java
│   └── TransferRequestDTO.java
│
├── services/
│   ├── BankAccountService.java       # Interface
│   └── BankAccountServiceImpl.java   # Implementation
│
├── entities/
│   ├── BankAccount.java              # Abstract — SINGLE_TABLE inheritance
│   ├── CurrentAccount.java
│   ├── SavingAccount.java
│   ├── Customer.java
│   └── AccountOperation.java
│
├── repositories/
│   ├── BankAccountRepository.java
│   ├── CustomerRepository.java
│   └── AccountOperationRepository.java
│
├── mappers/
│   └── BankAccountMapperImpl.java
│
├── enums/
│   ├── AccountStatus.java
│   └── OperationType.java
│
├── exceptions/
│   ├── BankAccountNotFoundException.java
│   ├── CustomerNotFoundException.java
│   └── BalanceNotSufficentException.java
│
└── EbankingBackendApplication.java
```

---

## Security Architecture

```
Client (Angular)
      │
      │  POST /auth/login  { username, password }
      ▼
AuthController
      │
      │  AuthenticationManager.authenticate()
      ▼
DaoAuthenticationProvider ──► InMemoryUserDetailsManager
      │
      │  on success → JwtUtils.generateToken()
      ▼
LoginResponseDTO  { accessToken, tokenType, username, roles }
      │
      ▼
Client stores the token and sends it on every request:
      Authorization: Bearer <token>
      │
      ▼
JwtAuthenticationFilter (OncePerRequestFilter)
      │  extracts token → validates → loads UserDetails
      │  sets Authentication in SecurityContextHolder
      ▼
SecurityConfig.authorizeHttpRequests()
      │  checks roles via @PreAuthorize
      ▼
Controller method executes
```

### Key design decisions

- **Stateless** — `SessionCreationPolicy.STATELESS`. No `HttpSession` is ever created.
- **CSRF disabled** — not needed for stateless JWT APIs.
- **CORS** — configured to allow `http://localhost:4200` (Angular dev server) with the `Authorization` header exposed.
- **Role separation** — `ROLE_USER` for all read and banking operations, `ROLE_ADMIN` for customer create / update / delete.
- **Method-level security** — `@EnableMethodSecurity(prePostEnabled = true)` enables `@PreAuthorize` on individual controller methods.

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL / MariaDB running locally

### 1. Clone the repository

```bash
git clone https://github.com/FatimaZahraLasfar/digital-banking-spring-backend.git
cd digital-banking-spring-backend
```

### 2. Configure the database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/E-BANK?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Configure the JWT secret

The secret must be a **Base64-encoded string of at least 32 bytes (256 bits)**. The default value in `application.properties` is safe for development. For production, replace it with a securely generated key:

```properties
application.security.jwt.secret-key=YOUR_BASE64_SECRET_HERE
application.security.jwt.expiration=86400000
```

To generate a strong secret:
```bash
openssl rand -base64 32
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The server starts on **port 8085**.

Swagger UI is available at: `http://localhost:8085/swagger-ui.html`

---

## API Endpoints

### Auth

| Method | URL | Auth required | Description |
|---|---|---|---|
| POST | `/auth/login` | No | Get a JWT token |

**Request body:**
```json
{
  "username": "user1",
  "password": "12345"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "username": "user1",
  "roles": ["ROLE_USER"]
}
```

---

### Customers

| Method | URL | Role | Description |
|---|---|---|---|
| GET | `/customers` | USER | List all customers |
| GET | `/customers/{id}` | USER | Get customer by ID |
| GET | `/customers/search?keyword=` | USER | Search customers by name |
| POST | `/customers` | ADMIN | Create a new customer |
| PUT | `/customers/{id}` | ADMIN | Update a customer |
| DELETE | `/customers/{id}` | ADMIN | Delete a customer |

---

### Bank Accounts

| Method | URL | Role | Description |
|---|---|---|---|
| GET | `/accounts` | USER | List all bank accounts |
| GET | `/accounts/{accountId}` | USER | Get account by ID |
| GET | `/accounts/{accountId}/operations` | USER | Get full operation history |
| GET | `/accounts/{accountId}/pageOperations?page=0&size=5` | USER | Get paginated operation history |
| POST | `/accounts/debit` | USER | Debit an account |
| POST | `/accounts/credit` | USER | Credit an account |
| POST | `/accounts/transfer` | USER | Transfer between accounts |

**Debit request body:**
```json
{
  "accountId": "uuid-here",
  "amount": 500.00,
  "description": "ATM withdrawal"
}
```

**Transfer request body:**
```json
{
  "accountSource": "uuid-source",
  "accountDestination": "uuid-dest",
  "amount": 1000.00,
  "description": "Monthly transfer"
}
```

---

## Authentication Flow

1. Send `POST /auth/login` with credentials.
2. Store the returned `accessToken` (e.g. in `localStorage` on the Angular side).
3. Attach it to every subsequent request:
   ```
   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
   ```
4. The `JwtAuthenticationFilter` validates the token and sets the security context automatically.
5. If the token is expired or invalid, the server returns `403 Forbidden`.

---

## Demo Users

Two in-memory users are configured for development in `SecurityConfig`:

| Username | Password | Roles |
|---|---|---|
| `user1` | `12345` | USER |
| `admin` | `12345` | USER, ADMIN |

> To connect to a real database instead, replace `InMemoryUserDetailsManager` in `SecurityConfig` with a custom `UserDetailsService` that loads users from a `UserRepository`.

---

## Commit History

| Commit message | File(s) |
|---|---|
| `feat(deps): add spring-boot-starter-security and JJWT 0.11.5 dependencies` | `pom.xml` |
| `feat(config): externalize JWT secret key and expiration to application.properties` | `application.properties` |
| `feat(security): add SecurityConfig with stateless JWT session, CORS, and route protection` | `SecurityConfig.java` |
| `feat(security): add JwtUtils for token generation, parsing, and validation` | `JwtUtils.java` |
| `feat(security): add JwtAuthenticationFilter to validate Bearer tokens on every request` | `JwtAuthenticationFilter.java` |
| `feat(dtos): add LoginRequestDTO and LoginResponseDTO for the auth endpoint` | `LoginRequestDTO.java`, `LoginResponseDTO.java` |
| `feat(auth): add POST /auth/login endpoint returning a signed JWT on success` | `AuthController.java` |
| `feat(dtos): add DebitDTO, CreditDTO, and TransferRequestDTO for operation endpoints` | `DebitDTO.java`, `CreditDTO.java`, `TransferRequestDTO.java` |
| `feat(api): add debit, credit, transfer endpoints and @PreAuthorize role guards` | `BankAccountRestController.java` |
| `feat(security): protect CustomerRestController — USER for reads, ADMIN for writes` | `CustomerRestController.java` |

---

## Author

**Fatima Zahra Lasfar** — based on the tutorial series by **Prof. Mohamed YOUSSFI**
- Backend Part 1 & 2: DAO layer, Service layer, REST Controllers
- Security Part: Spring Security + JWT (this branch)
