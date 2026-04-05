# E-Commerce Spring Boot Application - Copilot Instructions

## Project Overview

This is a Spring Boot 4.0.1 e-commerce application using Java 21, SQL Server, and Spring Security. The project follows a layered architecture with controllers, services, DTOs, and JPA entities.

**Key Tech Stack:**

- Spring Boot 4.0.1 (starter-webmvc, starter-data-jpa, starter-security)
- Java 21
- SQL Server (MSSQL) with Hibernate ORM
- Spring Security 7.0.2 with stateless session management
- Lombok for boilerplate reduction
- Maven build system

## Architecture Patterns

### Layered Structure

The codebase follows strict separation of concerns:

```
controller/ → service/ → repository/ → entity/
    ↓
  DTOs (request/response)
```

**Key Convention:** Controllers only accept/return DTOs (never entities). Services handle DTO-to-Entity mapping via dedicated mapper methods (e.g., `mapToEntity()`, `mapToResponse()` in `UserService.java`).

### DTO Validation Pattern

- Use `@Valid` annotation on controller parameters (e.g., `@RequestBody @Valid UserRequestDto`)
- Validation rules are defined in DTOs using Jakarta validation annotations:
  - `@NotBlank`, `@Email`, `@Size` with custom messages
  - `GlobalExceptionHandler` catches `MethodArgumentNotValidException` and returns field-level error map

### Exception Handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) handles:

- `EntityNotFoundException` → 404
- `DataIntegrityViolationException` → 409 (duplicate entries)
- `MethodArgumentNotValidException` → 400 with field-level error map
- Type mismatches and JSON parsing errors → 400

**Pattern:** Always use specific exception types; avoid generic exceptions.

### Service Layer Transactionality

Services use `@Transactional` at method level:

- Read-only queries: `@Transactional(readOnly = true)`
- Write operations: `@Transactional` (default rollback on exception)

Example: `UserService.getAllUsers()` uses readOnly=true; `newUser()` and `updateUser()` use full transactionality.

## Security Configuration

- **CSRF disabled** for stateless API
- **Session policy:** STATELESS (no cookies, stateless auth only)
- **Auth endpoints:** `/auth/**`, `/register`, `/login` are permit-all
- **Password encoding:** BCryptPasswordEncoder
- **Future auth:** Infrastructure is ready for JWT (SecurityConfig is pre-configured for extension)

## Entity Relationships (Discoverable from package structure)

The codebase includes these entities (currently UserEntity is fully implemented):

- UserEntity (complete with CRUD)
- OrderEntity, OrderItemEntity (order processing)
- CartEntity, CartItemEntity (shopping cart)
- ProductEntity, InventoryEntity (product catalog)
- PaymentEntity (payment tracking)
- ReviewEntity (product reviews)
- WishlistEntity, WishlistItemEntity (user wishlists)
- CategoryEntity, CouponEntity (e-commerce features)
- AddressEntity (user address management)

**Naming Convention:** All database tables use uppercase English names (e.g., `@Table(name = "Users")`).

## Build & Run Commands

### Maven Commands

```bash
./mvnw clean install        # Full build with tests
./mvnw spring-boot:run      # Run application locally
./mvnw test                 # Run all tests
./mvnw package              # Package as JAR (target/e-commerece-app-0.0.1-SNAPSHOT.jar)
```

### Database Setup (SQL Server)

- **Driver:** `com.microsoft.sqlserver.jdbc.SQLServerDriver`
- **Connection:** `jdbc:sqlserver://localhost:1433;databaseName=e-com;trustServerCertificate=true;integratedSecurity=true`
- **Hibernate DDL:** `spring.jpa.hibernate.ddl-auto=update` (auto-creates/updates schema)
- **Database:** Must have `e-com` database pre-created; integrated Windows authentication required

### DevTools

Application includes `spring-boot-devtools` for live reload during development.

## REST API Endpoint Pattern

Users endpoint example (pattern applies to all resources):

```
GET    /api/users           → List all users
GET    /api/users/{id}      → Get single user
POST   /api/users           → Create (returns 201 CREATED)
PUT    /api/users/{id}      → Update
DELETE /api/users/{id}      → Delete (returns 200)
```

Response format: `UserResponseDto` excludes password; errors return 4xx with descriptive messages.

## Lombok Usage

- Use `@RequiredArgsConstructor` for dependency injection in services/controllers (not traditional `@Autowired`)
- Entities use `@Builder` for flexible construction
- DTOs use `@Data` (includes getters/setters/equals/hashCode)
- Use `@Getter`/`@Setter` selectively in entities

## Development Workflow

1. Define DTOs first (request/response in separate classes)
2. Create entity with `@Entity`, `@Table`, Lombok annotations
3. Create repository extending `JpaRepository<Entity, ID>`
4. Create service with mapping logic and `@Transactional` methods
5. Create controller accepting DTOs, delegating to service
6. Global exception handler catches validation/database errors automatically

## Common Pitfalls to Avoid

- **Never return entities from controllers** — always map to DTOs first
- **Don't mix database logic in controllers** — use services
- **Password field:** DTOs have it; response DTO intentionally excludes it (security)
- **Database integration auth:** Uses Windows integrated security; Dockerfile/CI-CD must account for this
- **Package naming:** Note underscore in package `com.e_commere.e_commerece_app` (not hyphens)
