# CleanMe Backend - Technical Documentation

## 📋 Overview

CleanMe Backend is a robust Spring Boot application that provides REST API services for a cleaning service marketplace. It handles user authentication, cleaner profile management, service bookings, and real-time notifications. Built with Java 17, Spring Security, and PostgreSQL.

## 🏗️ Architecture

### Technology Stack

- **Framework**: Spring Boot 3.x
- **Language**: Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **Build Tool**: Maven
- **ORM**: Hibernate/JPA

### Project Structure

```
src/main/java/com/cleanme/
├── CleanmeApplication.java          # Main application class
├── configuration/                   # Configuration classes
│   ├── SecurityConfig.java         # Security configuration
│   ├── PasswordEncoderConfig.java  # Password encoding
│   └── WebConfig.java              # CORS configuration
├── controller/                      # REST controllers
│   ├── AuthController.java         # Authentication endpoints
│   ├── CleanerController.java      # Cleaner management
│   ├── ReservationController.java  # Booking management
│   └── NotificationController.java # Notifications
├── service/                         # Business logic
│   ├── AuthService.java            # Authentication service
│   ├── CleanerService.java         # Cleaner operations
│   ├── ReservationService.java     # Booking operations
│   └── NotificationService.java    # Notification handling
├── repository/                      # Data access layer
│   ├── UsersRepository.java        # User data access
│   ├── CleanerRepository.java      # Cleaner data access
│   └── ReservationRepository.java  # Reservation data access
├── entity/                          # JPA entities
│   ├── UsersEntity.java            # User entity
│   ├── CleanerDetailsEntity.java   # Cleaner details
│   └── ReservationEntity.java      # Reservation entity
├── dto/                             # Data Transfer Objects
│   ├── auth/                        # Authentication DTOs
│   ├── CleanerDetailsDto.java      # Cleaner response DTO
│   └── ReservationDto.java         # Reservation DTO
├── enums/                           # Enumeration classes
│   ├── UserType.java               # User type enum
│   └── ReservationStatus.java      # Booking status enum
├── security/                        # Security utilities
│   ├── JwtUtil.java                # JWT utility class
│   ├── JwtAuthenticationFilter.java # JWT filter
│   └── UserDetailsServiceImpl.java # User details service
└── utilities/                       # Utility classes
    ├── SecurityUtils.java          # Security helpers
    ├── BioConverter.java           # JSON converters
    └── AvailabilityConverter.java  # Availability converter
```

## 🔐 Authentication & Security

### JWT Authentication Flow

1. **User Registration/Login** - Credentials validated and JWT token generated
2. **Token Storage** - Frontend stores JWT token in localStorage
3. **API Requests** - Token sent in Authorization header: `Bearer {token}`
4. **Token Validation** - Backend validates token and extracts user information
5. **Security Context** - User authentication set in Spring Security context

### Security Configuration

The application uses Spring Security with JWT for stateless authentication:

```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                .requestMatchers("/auth/cleaner-setup").authenticated()
                .requestMatchers(HttpMethod.PUT, "/cleaners/**").authenticated()
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

### Authorization Levels

- **Public**: `/auth/register`, `/auth/login`
- **Authenticated**: All cleaner management and booking operations
- **Role-based**: Future enhancement for admin operations

### JWT Implementation

```java
@Component
public class JwtUtil {

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
```

## 📡 API Endpoints

### Authentication Endpoints

#### Register User

```http
POST /auth/register
Content-Type: application/json

Request Body:
{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "securePassword123",
    "phoneNumber": "+38761234567",
    "address": "Sarajevo, Bosnia and Herzegovina",
    "userType": "CLEANER"
}

Response: 200 OK
{
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "uid": "550e8400-e29b-41d4-a716-446655440000",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "userType": "CLEANER"
}
```

#### User Login

```http
POST /auth/login
Content-Type: application/json

Request Body:
{
    "email": "john.doe@example.com",
    "password": "securePassword123"
}

Response: 200 OK
{
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "uid": "550e8400-e29b-41d4-a716-446655440000",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "userType": "CLEANER"
}
```

#### Cleaner Setup

```http
POST /auth/cleaner-setup
Authorization: Bearer {jwt_token}
Content-Type: application/json

Request Body:
{
    "cleanerId": "550e8400-e29b-41d4-a716-446655440000",
    "servicesOffered": "Deep House Cleaning, Office Cleaning",
    "hourlyRate": 35.00,
    "availability": [
        {"Monday": {"from": "09:00", "to": "17:00"}},
        {"Tuesday": {"from": "09:00", "to": "17:00"}}
    ],
    "bio": ["Professional cleaning service with 5 years experience"]
}

Response: 200 OK
```

### Cleaner Management Endpoints

#### Get All Cleaners

```http
GET /cleaners
Authorization: Bearer {jwt_token}

Response: 200 OK
[
    {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "servicesOffered": "Deep House Cleaning, Office Cleaning",
        "hourlyRate": 35.00,
        "availability": [...],
        "bio": ["Professional cleaning service with 5 years experience"]
    }
]
```

#### Get Cleaner by ID

```http
GET /cleaners/{id}
Authorization: Bearer {jwt_token}

Response: 200 OK - Cleaner details
Response: 404 Not Found - Cleaner details not found
```

#### Update Cleaner Details

```http
PUT /cleaners/{id}
Authorization: Bearer {jwt_token}
Content-Type: application/json

Request Body:
{
    "servicesOffered": "Deep House Cleaning, Window Cleaning",
    "hourlyRate": 40.00,
    "bio": ["Updated professional cleaning service"]
}

Response: 200 OK
Response: 404 Not Found - Cleaner not found
```

#### Filter Cleaners

```http
GET /cleaners/filter
Authorization: Bearer {jwt_token}
Content-Type: application/json

Request Body:
{
    "minRate": 20.00,
    "maxRate": 50.00,
    "availability": "Monday"
}

Response: 200 OK - Filtered cleaner list
```

### Reservation Endpoints

#### Get All Reservations

```http
GET /reservation/all
Authorization: Bearer {jwt_token}

Response: 200 OK
[
    {
        "rid": "reservation-uuid",
        "date": "2024-12-15",
        "time": "10:00:00",
        "location": "Sarajevo",
        "status": "PENDING",
        "comment": "Deep cleaning needed",
        "cleanerName": "John Doe",
        "clientName": "Jane Smith",
        "clientPhone": "+38761234568"
    }
]
```

#### Create Reservation

```http
POST /reservation
Authorization: Bearer {jwt_token}
Content-Type: application/json

Request Body:
{
    "date": "2024-12-15",
    "time": "10:00:00",
    "location": "Sarajevo",
    "status": "PENDING",
    "comment": "Deep cleaning needed",
    "cleanerID": "cleaner-uuid"
}

Response: 201 Created
```

## 💾 Data Models

### Database Schema

The application uses PostgreSQL with the following main tables:

```sql
-- Users table (both cleaners and clients)
CREATE TABLE users (
    uid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    address VARCHAR(255),
    country VARCHAR(255),
    city VARCHAR(255),
    street VARCHAR(255),
    street_extra VARCHAR(255),
    user_type VARCHAR(255) NOT NULL
);

-- Cleaner details table
CREATE TABLE cleaner_details (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cleaner_id UUID NOT NULL REFERENCES users(uid),
    services_offered VARCHAR(255),
    hourly_rate DECIMAL(19,2),
    availability TEXT, -- JSON format
    bio TEXT -- JSON array format
);

-- Reservations table
CREATE TABLE reservations (
    rid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(uid),
    cleaner UUID NOT NULL REFERENCES users(uid),
    date DATE,
    time TIME,
    location VARCHAR(255),
    status VARCHAR(255),
    comment VARCHAR(255)
);

-- Reviews table
CREATE TABLE reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reservation_id UUID NOT NULL REFERENCES reservations(rid),
    cleaner_id UUID NOT NULL REFERENCES users(uid),
    user_id UUID NOT NULL REFERENCES users(uid),
    rating INTEGER NOT NULL,
    comment TEXT,
    date DATE NOT NULL
);

-- Favourites table
CREATE TABLE favourites (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL REFERENCES users(uid),
    cleaner_id UUID NOT NULL REFERENCES users(uid),
    UNIQUE(client_id, cleaner_id)
);

-- Notifications table
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(uid),
    message VARCHAR(255),
    read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Entity Relationships

- **Users** ↔ **CleanerDetails** (One-to-One for cleaners)
- **Users** ↔ **Reservations** (One-to-Many as client)
- **Users** ↔ **Reservations** (One-to-Many as cleaner)
- **Users** ↔ **Reviews** (One-to-Many)
- **Users** ↔ **Favourites** (Many-to-Many)
- **Users** ↔ **Notifications** (One-to-Many)

### Key Entity Classes

#### UsersEntity

```java
@Entity
@Table(name = "users")
@Data
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uid;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    // Additional fields and relationships...
}
```

#### CleanerDetailsEntity

```java
@Entity
@Table(name = "cleaner_details")
@Data
public class CleanerDetailsEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "cleaner_id", referencedColumnName = "UID")
    private UsersEntity cleaner;

    @Column(name = "services_offered")
    private String servicesOffered;

    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;

    @Column(name = "availability")
    @Convert(converter = AvailabilityConverter.class)
    private List<Map<String, TimeRange>> availability;

    @Column(name = "bio")
    @Convert(converter = BioConverter.class)
    private List<String> bio;
}
```

## 🔧 Configuration

### Application Properties

```properties
# Application Name
spring.application.name=cleanme

# Server Configuration
server.port=${PORT:8080}

# Database Configuration
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/cleanme}
spring.datasource.username=${JDBC_DB_USERNAME:postgres}
spring.datasource.password=${JDBC_DB_PASSWORD:password}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration
jwt.secret=${JDBC_JW_SECRET:defaultSecret}
jwt.expiration=86400000

# CORS Configuration
frontend.url=${FRONTEND_URL:http://localhost:4200}

# Logging
spring.main.banner-mode=off
logging.level.root=warn
```

### Environment Variables

```bash
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/cleanme
JDBC_DB_USERNAME=postgres
JDBC_DB_PASSWORD=your_password

# JWT
JDBC_JW_SECRET=your_jwt_secret_key_here

# Frontend URL for CORS
FRONTEND_URL=http://localhost:4200

# Port (for deployment)
PORT=8080
```

### CORS Configuration

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendUrl)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

## 🚨 Error Handling

### HTTP Status Codes

- **200 OK**: Successful requests
- **201 Created**: Resource created successfully
- **400 Bad Request**: Validation errors
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Access denied
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server errors

### Common Error Scenarios

1. **Email Already Exists**: During registration
2. **Invalid Credentials**: During login
3. **Cleaner Details Not Found**: For new users (handled gracefully)
4. **User Not Found**: Invalid user ID
5. **User Not Cleaner**: Operations on non-cleaner users
6. **JWT Token Invalid**: Authentication failures

## 🧪 Testing

### Test Structure

```
src/test/java/com/cleanme/
├── controller/          # Controller integration tests
├── service/            # Service unit tests
├── repository/         # Repository tests
└── integration/        # Full integration tests
```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=CleanerServiceTest

# Run with coverage
./mvnw test jacoco:report

# Run integration tests
./mvnw test -Dtest=*IT
```

## 🚀 Deployment

### Local Development Setup

```bash
# 1. Clone the repository
git clone <repository-url>
cd cleanme-backend

# 2. Set up PostgreSQL database
docker run --name cleanme-postgres \
  -e POSTGRES_DB=cleanme \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 -d postgres:15

# 3. Set environment variables
export DATABASE_URL=jdbc:postgresql://localhost:5432/cleanme
export JDBC_DB_USERNAME=postgres
export JDBC_DB_PASSWORD=password
export JDBC_JW_SECRET=your-secret-key
export FRONTEND_URL=http://localhost:4200

# 4. Run the application
./mvnw spring-boot:run
```

### Production Build

```bash
# Build JAR file
./mvnw clean package -DskipTests

# Run JAR
java -jar target/cleanme-backend-*.jar
```

### Docker Deployment

```dockerfile
# Dockerfile
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file
COPY target/cleanme-backend-*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose

```yaml
version: "3.8"

services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: cleanme
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      DATABASE_URL: jdbc:postgresql://postgres:5432/cleanme
      JDBC_DB_USERNAME: postgres
      JDBC_DB_PASSWORD: password
      JDBC_JW_SECRET: your-secret-key
      FRONTEND_URL: http://localhost:4200
    depends_on:
      - postgres

volumes:
  postgres_data:
```

### Heroku Deployment

```bash
# 1. Create Heroku app
heroku create cleanme-backend

# 2. Add PostgreSQL addon
heroku addons:create heroku-postgresql:mini

# 3. Set environment variables
heroku config:set JDBC_JW_SECRET=your-secret-key
heroku config:set FRONTEND_URL=https://your-frontend-domain.com

# 4. Deploy
git push heroku main
```

## 📋 Development Guidelines

### Code Style

- Follow Spring Boot conventions
- Use Lombok for reducing boilerplate code
- Implement proper exception handling
- Write comprehensive unit tests
- Document complex business logic

### Database Best Practices

- Use appropriate data types and constraints
- Implement proper indexing for frequently queried fields
- Use transactions for multi-step operations
- Handle concurrent access scenarios
- Implement soft deletes where appropriate

### Security Best Practices

- Never expose sensitive information in logs
- Validate all input data
- Use parameterized queries to prevent SQL injection
- Implement proper CORS configuration
- Secure JWT secret keys
- Use HTTPS in production

### Performance Considerations

- Implement caching for frequently accessed data
- Use database indexes appropriately
- Optimize N+1 queries with proper JPA fetching
- Monitor application performance
- Implement pagination for large datasets

## 📚 Additional Resources

### Documentation

- [Spring Boot Reference](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://spring.io/projects/spring-security)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Maven Documentation](https://maven.apache.org/)

### Development Tools

- **IDE**: IntelliJ IDEA or Eclipse
- **Database**: PostgreSQL with pgAdmin
- **API Testing**: Postman or Insomnia
- **Build**: Maven
- **Version Control**: Git

### Monitoring and Observability

- Spring Boot Actuator for health checks
- Micrometer for metrics
- Logback for structured logging
- Application performance monitoring tools

---

**Version**: 1.0.0  
**Last Updated**: December 2024  
**Maintainer**: CleanMe Development Team
