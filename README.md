# Spring User Management

A Spring Boot 3 application that demonstrates user registration, JWT-based authentication, role/permission-based authorization, and a sample Weather API secured with method-level security.

## Tech Stack
- **Java**: 17
- **Spring Boot**: 3.5.5
- **Spring Modules**: Web, Security, Data JPA, Thymeleaf
- **Database**: H2 (in-memory)
- **Auth**: JWT (jjwt 0.11.5)
- **Build**: Maven

## Features
- **User registration** (public) and **admin creation** (secured by role)
- **JWT authentication** endpoint to obtain tokens
- **Method-level security** using `@PreAuthorize` with roles and permissions
- **Sample Weather CRUD** endpoints protected by permissions
- **Thymeleaf demo page** at `/app/weather` (renders `templates/index.html`)

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run (dev)
```bash
mvn spring-boot:run
```

### Build and run jar
```bash
mvn clean package
java -jar target/spring-user-management-0.0.1-SNAPSHOT.jar
```

## Configuration
Default configuration is minimal. For easier development, you can enable the H2 console and show SQL:

```properties
# src/main/resources/application.properties
spring.application.name=spring-user-management

# Dev-friendly settings
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Access H2 console at `http://localhost:8080/h2-console` and use the default JDBC URL suggested on the page (usually `jdbc:h2:mem:testdb`).

## Domain & Security Model
- `Users` implements Spring Security `UserDetails` and stores `username`, `password`, and `role`.
- `Role` enum: `ADMIN`, `USER`, `GUEST`.
- `Permissions` enum: `WEATHER_READ`, `WEATHER_WRITE`, `WEATHER_DELETE`.
- Authorities are composed of a role authority (ROLE_...) plus all permissions granted by the role.

## Auth Flow
1. **Register a user** (public): choose a role (e.g., ADMIN)
2. **Authenticate** with username/password -> receive JWT
3. **Use JWT** in `Authorization: Bearer <token>` header for secured endpoints

### Sample Requests

1) Register user (public)
```bash
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"pass","role":"ADMIN"}'
```

2) Authenticate
```bash
curl -X POST http://localhost:8080/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"pass"}'
```
Response body is the JWT token string.

3) Call secured endpoint
```bash
TOKEN=... # paste token from step 2
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/weather/all
```

## API Endpoints

- Public
  - `POST /api/user/register` – register new user
  - `POST /api/authenticate` – get JWT token

- Role/Permission Secured
  - `POST /api/user/create/admin` – requires `ROLE_ADMIN`
  - Weather (requires permissions):
    - `GET /api/weather/forCity?city=Oslo` – `WEATHER_READ`
    - `POST /api/weather/save` – `WEATHER_WRITE`
      ```json
      {"city":"Oslo","forecast":"Sunny"}
      ```
    - `GET /api/weather/all` – `WEATHER_READ`
    - `PUT /api/weather/{city}?foreCast=Rain` – `WEATHER_WRITE`
    - `DELETE /api/weather/deleteCity?city=Oslo` – `WEATHER_DELETE`

## Notes for Developers
- **Security config**: JWT filter is added before `UsernamePasswordAuthenticationFilter`. Public routes include `/api/authenticate`, `/api/user/**`, and `/h2-console/**`.
- **Duplicate bean caution**: Do not declare `@Bean customUserDetailsService()` if `CustomUserDetailsService` is annotated with `@Service`; otherwise you may get `NoUniqueBeanDefinitionException`.
- **Role authority prefix**: Spring expects role authorities like `ROLE_ADMIN`. Ensure your `Users#getAuthorities()` adds `"ROLE_" + role.name()`.
- **JWT utils**: Token expiration is enforced; include `Authorization: Bearer <token>` on protected requests.

## Troubleshooting
- 401 Unauthorized
  - Missing/invalid `Authorization` header
  - Expired token – re-authenticate
- 403 Forbidden
  - Authenticated but lacking required role/permission
- H2 Console not loading
  - Enable console in `application.properties` and permit `/h2-console/**` in security config
- Ambiguous injection errors
  - Ensure only one bean of a type unless using `@Primary` or `@Qualifier`

## Project Structure (high level)
```
src/main/java/com/manage/springusermanagement
├─ SpringUserManagementApplication.java
├─ controller/ (REST + MVC)
├─ config/ (Security + other configs)
├─ entity/ (Users, Role, Permissions, Weather)
├─ repo/ (UserRepo, WeatherRepo)
├─ service/ (User and Weather services)
├─ filter/ (JwtAuthenticationFilter)
└─ utils/ (JWTUtils)

src/main/resources
├─ templates/index.html
└─ application.properties
```

