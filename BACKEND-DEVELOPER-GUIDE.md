# 🧹 CleanMe Backend Developer Guide

> **Comprehensive guide for developers working on the CleanMe Spring Boot REST API**

## 🎯 Table of Contents

1. [🚀 Quick Start & Development](#-quick-start--development)
2. [🏗️ Architecture Overview](#️-architecture-overview)
3. [📦 Package Structure](#-package-structure)
4. [🗄️ Data Model & Entities](#️-data-model--entities)
5. [🔒 Security & Authentication](#-security--authentication)
6. [📡 REST API Design](#-rest-api-design)
7. [💼 Business Logic Patterns](#-business-logic-patterns)
8. [🗃️ Database Design](#️-database-design)
9. [🧪 Testing Strategy](#-testing-strategy)
10. [⚡ Performance & Optimization](#-performance--optimization)
11. [🚀 Deployment & Environment](#-deployment--environment)
12. [🛠️ Development Tools](#️-development-tools)

---

## 🚀 Quick Start & Development

### Development Environment Setup

```bash
# Prerequisites
- Java 21
- Maven 3.8+
- PostgreSQL 14+
- IDE (IntelliJ IDEA recommended)

# Clone and setup
git clone <repository-url>
cd cleanme-backend

# Install dependencies
mvn clean install

# Run development server
mvn spring-boot:run

# API available at http://localhost:8080
```

### Essential IDE Configuration

```xml
<!-- IntelliJ IDEA Settings -->
<settings>
  <component name="ProjectCodeStyleSettingsManager">
    <option name="PER_PROJECT_SETTINGS">
      <value>
        <option name="CLASS_BRACE_STYLE" value="2" />
        <option name="METHOD_BRACE_STYLE" value="2" />
      </value>
    </option>
  </component>
</settings>
```

### Environment Variables

```bash
# Required Environment Variables
DATABASE_URL=jdbc:postgresql://localhost:5432/cleanme
JDBC_DB_USERNAME=postgres
JDBC_DB_PASSWORD=your_password
JDBC_JW_SECRET=your-secret-key-256-bits-minimum
FRONTEND_URL=http://localhost:4200
```

---

## 🏗️ Architecture Overview

### 📊 System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend Layer                       │
│              Angular (localhost:4200)                   │
└─────────────────────┬───────────────────────────────────┘
                      │ HTTP/REST + JWT
┌─────────────────────▼───────────────────────────────────┐
│                  API Gateway Layer                      │
│           Spring Security + CORS                        │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│               Controller Layer                          │
│    @RestController + @RequestMapping                    │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│               Service Layer                             │
│         @Service + Business Logic                       │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│             Repository Layer                            │
│        JpaRepository + Custom Queries                   │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│               Database Layer                            │
│            PostgreSQL + JPA/Hibernate                   │
└─────────────────────────────────────────────────────────┘
```

### 🎯 Design Principles

#### 1. **Layered Architecture**

```java
@RestController  // Presentation Layer
@Service        // Business Logic Layer
@Repository     // Data Access Layer
@Entity         // Domain Model
```

#### 2. **Dependency Injection**

```java
@Service
public class CleanerService {
    private final CleanerRepository cleanerRepository;
    private final UserService userService;

    public CleanerService(CleanerRepository cleanerRepository,
                         UserService userService) {
        this.cleanerRepository = cleanerRepository;
        this.userService = userService;
    }
}
```

#### 3. **DTO Pattern**

```java
// Entity (Internal)
@Entity
public class UsersEntity { /* ... */ }

// DTO (External)
public class UserRegistrationDto { /* ... */ }
public class UserResponseDto { /* ... */ }
```

---

## 📦 Package Structure

### 🏛️ Project Organization

```
src/main/java/com/cleanme/
├── 🎮 controller/           # REST endpoints
│   ├── AuthController.java
│   ├── UserController.java
│   ├── CleanerController.java
│   ├── ReservationController.java
│   ├── ReviewController.java
│   ├── FavouriteController.java
│   └── NotificationController.java
├── 💼 service/              # Business logic
│   ├── AuthService.java
│   ├── UserService.java
│   ├── CleanerService.java
│   ├── ReservationService.java
│   ├── ReviewService.java
│   ├── FavouriteService.java
│   └── NotificationService.java
├── 🗃️ repository/           # Data access
│   ├── UserRepository.java
│   ├── CleanerRepository.java
│   ├── ReservationRepository.java
│   ├── ReviewRepository.java
│   ├── FavouriteRepository.java
│   └── NotificationRepository.java
├── 🏢 entity/               # JPA entities
│   ├── UsersEntity.java
│   ├── CleanerDetailsEntity.java
│   ├── ReservationEntity.java
│   ├── ReviewEntity.java
│   ├── FavouriteEntity.java
│   └── NotificationEntity.java
├── 📦 dto/                  # Data transfer objects
│   ├── request/
│   ├── response/
│   └── mapper/
├── 🔒 security/             # Security configuration
│   ├── SecurityConfig.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtUtils.java
├── ⚙️ configuration/        # Application config
│   ├── WebConfig.java
│   └── DatabaseConfig.java
├── 🏷️ enums/                # Enumerations
│   ├── UserType.java
│   └── ReservationStatus.java
├── ❌ exception/            # Exception handling
│   ├── GlobalExceptionHandler.java
│   └── CustomExceptions.java
├── 🛠️ utilities/            # Utility classes
│   └── DateUtils.java
└── CleanmeApplication.java  # Main application
```

### 📋 Package Conventions

```java
// Controller naming
@RestController
@RequestMapping("/api/cleaners")
public class CleanerController { }

// Service naming
@Service
@Transactional
public class CleanerService { }

// Repository naming
@Repository
public interface CleanerRepository extends JpaRepository<CleanerDetailsEntity, String> { }

// Entity naming
@Entity
@Table(name = "cleaner_details")
public class CleanerDetailsEntity { }
```

---

## 🗄️ Data Model & Entities

### 🏢 Core Entities

#### UsersEntity

```java
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

#### CleanerDetailsEntity

```java
@Entity
@Table(name = "cleaner_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleanerDetailsEntity {
    @Id
    private String id; // Same as user ID

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "services_offered")
    private String servicesOffered;

    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;

    @Column(name = "min_hours")
    private Integer minHours;

    @ElementCollection
    @CollectionTable(name = "cleaner_zones")
    private List<String> zones;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private UsersEntity user;
}
```

#### ReservationEntity

```java
@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "cleaner_id", nullable = false)
    private CleanerDetailsEntity cleaner;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

### 🔗 Entity Relationships

```java
// One-to-One: User ↔ CleanerDetails
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
private CleanerDetailsEntity cleanerDetails;

// One-to-Many: User → Reservations
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
private List<ReservationEntity> reservations;

// Many-to-One: Reservation → User
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private UsersEntity user;
```

---

## 🔒 Security & Authentication

### 🔐 JWT Authentication Flow

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {

        String token = extractTokenFromRequest(request);

        if (token != null && jwtUtils.validateToken(token)) {
            String username = jwtUtils.getUsernameFromToken(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
```

### 🛡️ Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cleaners/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(frontendUrl));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### 🔑 Password Security

```java
@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationDto dto) {
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        // Save user with hashed password
    }

    public boolean authenticateUser(String email, String password) {
        UsersEntity user = userRepository.findByEmail(email);
        return passwordEncoder.matches(password, user.getPassword());
    }
}
```

---

## 📡 REST API Design

### 🎯 API Conventions

#### RESTful URL Structure

```java
// Resource-based URLs
GET    /api/cleaners           # Get all cleaners
GET    /api/cleaners/{id}      # Get specific cleaner
POST   /api/cleaners           # Create cleaner
PUT    /api/cleaners/{id}      # Update cleaner
DELETE /api/cleaners/{id}      # Delete cleaner

// Nested resources
GET    /api/cleaners/{id}/reviews      # Get cleaner reviews
POST   /api/reservations               # Create reservation
GET    /api/users/{id}/reservations    # Get user reservations
```

#### HTTP Status Codes

```java
@RestController
public class CleanerController {

    @GetMapping("/{id}")
    public ResponseEntity<CleanerResponseDto> getCleaner(@PathVariable String id) {
        try {
            CleanerResponseDto cleaner = cleanerService.getCleanerById(id);
            return ResponseEntity.ok(cleaner);                    // 200 OK
        } catch (CleanerNotFoundException e) {
            return ResponseEntity.notFound().build();            // 404 Not Found
        }
    }

    @PostMapping
    public ResponseEntity<CleanerResponseDto> createCleaner(@Valid @RequestBody CleanerCreateDto dto) {
        CleanerResponseDto created = cleanerService.createCleaner(dto);
        return ResponseEntity.status(HttpStatus.CREATED)         // 201 Created
                           .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CleanerResponseDto> updateCleaner(
            @PathVariable String id,
            @Valid @RequestBody CleanerUpdateDto dto) {
        try {
            CleanerResponseDto updated = cleanerService.updateCleaner(id, dto);
            return ResponseEntity.ok(updated);                   // 200 OK
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().build();          // 400 Bad Request
        }
    }
}
```

### 📊 Request/Response Patterns

#### Authentication Endpoints

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserRegistrationDto dto) {
        AuthResponseDto response = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        AuthResponseDto response = authService.authenticate(dto);
        return ResponseEntity.ok(response);
    }
}
```

#### Reservation Management

```java
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {

        Pageable pageable = PageRequest.of(page, size);
        List<ReservationResponseDto> reservations =
            reservationService.getAllReservations(pageable, status);

        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @Valid @RequestBody ReservationCreateDto dto,
            Authentication authentication) {

        String userId = authentication.getName();
        ReservationResponseDto created = reservationService.createReservation(dto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
```

---

## 💼 Business Logic Patterns

### 🎯 Service Layer Design

#### Service Implementation Pattern

```java
@Service
@Transactional
public class CleanerService {
    private final CleanerRepository cleanerRepository;
    private final UserRepository userRepository;
    private final CleanerMapper cleanerMapper;

    public CleanerService(CleanerRepository cleanerRepository,
                         UserRepository userRepository,
                         CleanerMapper cleanerMapper) {
        this.cleanerRepository = cleanerRepository;
        this.userRepository = userRepository;
        this.cleanerMapper = cleanerMapper;
    }

    @Transactional(readOnly = true)
    public List<CleanerCardDto> getAllCleaners() {
        List<CleanerDetailsEntity> cleaners = cleanerRepository.findAll();
        return cleaners.stream()
                      .map(cleanerMapper::toCardDto)
                      .collect(Collectors.toList());
    }

    @Transactional
    public CleanerResponseDto updateCleaner(String id, CleanerUpdateDto dto) {
        CleanerDetailsEntity cleaner = cleanerRepository.findById(id)
            .orElseThrow(() -> new CleanerNotFoundException("Cleaner not found with id: " + id));

        updateCleanerFromDto(cleaner, dto);
        CleanerDetailsEntity saved = cleanerRepository.save(cleaner);

        return cleanerMapper.toResponseDto(saved);
    }

    private void updateCleanerFromDto(CleanerDetailsEntity cleaner, CleanerUpdateDto dto) {
        if (dto.getBio() != null) {
            cleaner.setBio(dto.getBio());
        }
        if (dto.getHourlyRate() != null) {
            validateHourlyRate(dto.getHourlyRate());
            cleaner.setHourlyRate(dto.getHourlyRate());
        }
        if (dto.getServicesOffered() != null) {
            cleaner.setServicesOffered(String.join(",", dto.getServicesOffered()));
        }
    }

    private void validateHourlyRate(BigDecimal hourlyRate) {
        if (hourlyRate.compareTo(BigDecimal.valueOf(5)) < 0 ||
            hourlyRate.compareTo(BigDecimal.valueOf(200)) > 0) {
            throw new ValidationException("Hourly rate must be between 5 and 200");
        }
    }
}
```

### 🔄 Transaction Management

```java
@Service
@Transactional
public class ReservationService {

    @Transactional
    public ReservationResponseDto createReservation(ReservationCreateDto dto, String userId) {
        // Validate availability
        validateTimeSlotAvailability(dto.getCleanerId(), dto.getDate(), dto.getTime());

        // Create reservation
        ReservationEntity reservation = createReservationEntity(dto, userId);
        ReservationEntity saved = reservationRepository.save(reservation);

        // Send notification
        notificationService.sendReservationNotification(saved);

        return reservationMapper.toResponseDto(saved);
    }

    @Transactional
    public ReservationResponseDto updateReservationStatus(String id, ReservationStatus status) {
        ReservationEntity reservation = findReservationById(id);

        validateStatusTransition(reservation.getStatus(), status);
        reservation.setStatus(status);

        ReservationEntity updated = reservationRepository.save(reservation);

        // Send status update notification
        notificationService.sendStatusUpdateNotification(updated);

        return reservationMapper.toResponseDto(updated);
    }
}
```

### 📊 Data Mapping Patterns

```java
@Component
public class CleanerMapper {

    public CleanerResponseDto toResponseDto(CleanerDetailsEntity entity) {
        if (entity == null) return null;

        return CleanerResponseDto.builder()
            .id(entity.getId())
            .fullName(entity.getUser().getFirstName() + " " + entity.getUser().getLastName())
            .bio(entity.getBio())
            .servicesOffered(parseServices(entity.getServicesOffered()))
            .hourlyRate(entity.getHourlyRate())
            .minHours(entity.getMinHours())
            .zones(entity.getZones())
            .rating(calculateAverageRating(entity.getId()))
            .reviewCount(getReviewCount(entity.getId()))
            .build();
    }

    public CleanerCardDto toCardDto(CleanerDetailsEntity entity) {
        return CleanerCardDto.builder()
            .id(entity.getId())
            .fullName(getFullName(entity.getUser()))
            .rating(calculateAverageRating(entity.getId()))
            .location(getFirstZone(entity.getZones()))
            .shortBio(truncateBio(entity.getBio()))
            .services(parseServices(entity.getServicesOffered()))
            .price(entity.getHourlyRate())
            .currency("BAM")
            .build();
    }

    private List<String> parseServices(String servicesString) {
        if (servicesString == null || servicesString.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(servicesString.split(","))
                    .stream()
                    .map(String::trim)
                    .collect(Collectors.toList());
    }
}
```

---

## 🗃️ Database Design

### 📊 Database Schema

```sql
-- Users table
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('CLIENT', 'CLEANER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Cleaner details table
CREATE TABLE cleaner_details (
    id VARCHAR(36) PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    bio TEXT,
    services_offered TEXT,
    hourly_rate DECIMAL(10,2),
    min_hours INTEGER DEFAULT 2,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Cleaner zones table
CREATE TABLE cleaner_zones (
    cleaner_details_id VARCHAR(36) REFERENCES cleaner_details(id) ON DELETE CASCADE,
    zones VARCHAR(100),
    PRIMARY KEY (cleaner_details_id, zones)
);

-- Reservations table
CREATE TABLE reservations (
    id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    cleaner_id VARCHAR(36) NOT NULL REFERENCES cleaner_details(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    time TIME NOT NULL,
    location TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED')),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Reviews table
CREATE TABLE reviews (
    id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    reservation_id VARCHAR(36) UNIQUE NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    cleaner_id VARCHAR(36) NOT NULL REFERENCES cleaner_details(id) ON DELETE CASCADE,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Favourites table
CREATE TABLE favourites (
    id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    cleaner_id VARCHAR(36) NOT NULL REFERENCES cleaner_details(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, cleaner_id)
);

-- Notifications table
CREATE TABLE notifications (
    id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 🔍 Query Optimization

```java
@Repository
public interface CleanerRepository extends JpaRepository<CleanerDetailsEntity, String> {

    // Optimized query with JOIN FETCH to avoid N+1 problem
    @Query("SELECT c FROM CleanerDetailsEntity c " +
           "JOIN FETCH c.user u " +
           "LEFT JOIN FETCH c.zones " +
           "WHERE c.hourlyRate BETWEEN :minRate AND :maxRate")
    List<CleanerDetailsEntity> findCleanersByHourlyRateRange(
        @Param("minRate") BigDecimal minRate,
        @Param("maxRate") BigDecimal maxRate
    );

    // Custom query for cleaner search with filters
    @Query("SELECT c FROM CleanerDetailsEntity c " +
           "JOIN c.user u " +
           "WHERE (:zones IS NULL OR EXISTS (SELECT z FROM c.zones z WHERE z IN :zones)) " +
           "AND (:services IS NULL OR c.servicesOffered LIKE %:services%) " +
           "AND c.hourlyRate BETWEEN :minRate AND :maxRate")
    Page<CleanerDetailsEntity> findCleanersWithFilters(
        @Param("zones") List<String> zones,
        @Param("services") String services,
        @Param("minRate") BigDecimal minRate,
        @Param("maxRate") BigDecimal maxRate,
        Pageable pageable
    );

    // Native query for complex statistics
    @Query(value = "SELECT c.id, u.first_name, u.last_name, " +
                   "AVG(r.rating) as avg_rating, COUNT(r.id) as review_count " +
                   "FROM cleaner_details c " +
                   "JOIN users u ON c.id = u.id " +
                   "LEFT JOIN reviews r ON c.id = r.cleaner_id " +
                   "GROUP BY c.id, u.first_name, u.last_name " +
                   "HAVING COUNT(r.id) > 0 " +
                   "ORDER BY avg_rating DESC",
           nativeQuery = true)
    List<Object[]> findCleanersWithRatings();
}
```

---

## 🧪 Testing Strategy

### 🔬 Unit Testing Pattern

```java
@ExtendWith(MockitoExtension.class)
class CleanerServiceTest {

    @Mock
    private CleanerRepository cleanerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CleanerMapper cleanerMapper;

    @InjectMocks
    private CleanerService cleanerService;

    @Test
    void getAllCleaners_ShouldReturnCleanerList() {
        // Given
        List<CleanerDetailsEntity> mockCleaners = createMockCleaners();
        List<CleanerCardDto> expectedDtos = createMockCleanerDtos();

        when(cleanerRepository.findAll()).thenReturn(mockCleaners);
        when(cleanerMapper.toCardDto(any())).thenReturn(expectedDtos.get(0));

        // When
        List<CleanerCardDto> result = cleanerService.getAllCleaners();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFullName()).isEqualTo("John Doe");
        verify(cleanerRepository).findAll();
        verify(cleanerMapper).toCardDto(any());
    }

    @Test
    void updateCleaner_WithInvalidHourlyRate_ShouldThrowValidationException() {
        // Given
        String cleanerId = "test-id";
        CleanerUpdateDto dto = CleanerUpdateDto.builder()
            .hourlyRate(BigDecimal.valueOf(250)) // Invalid rate
            .build();

        CleanerDetailsEntity existingCleaner = createMockCleaner();
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.of(existingCleaner));

        // When & Then
        assertThatThrownBy(() -> cleanerService.updateCleaner(cleanerId, dto))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Hourly rate must be between 5 and 200");
    }
}
```

### 🎯 Integration Testing

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class CleanerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CleanerRepository cleanerRepository;

    @Test
    void createCleaner_WithValidData_ShouldReturnCreated() {
        // Given
        CleanerCreateDto createDto = CleanerCreateDto.builder()
            .bio("Professional cleaner")
            .hourlyRate(BigDecimal.valueOf(25))
            .servicesOffered(Arrays.asList("House Cleaning", "Office Cleaning"))
            .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getValidJwtToken());

        HttpEntity<CleanerCreateDto> request = new HttpEntity<>(createDto, headers);

        // When
        ResponseEntity<CleanerResponseDto> response = restTemplate.postForEntity(
            "/api/cleaners", request, CleanerResponseDto.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBio()).isEqualTo("Professional cleaner");
    }
}
```

### 🗃️ Repository Testing

```java
@DataJpaTest
class CleanerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CleanerRepository cleanerRepository;

    @Test
    void findCleanersByHourlyRateRange_ShouldReturnMatchingCleaners() {
        // Given
        CleanerDetailsEntity cleaner1 = createCleaner("cleaner1", BigDecimal.valueOf(20));
        CleanerDetailsEntity cleaner2 = createCleaner("cleaner2", BigDecimal.valueOf(30));
        CleanerDetailsEntity cleaner3 = createCleaner("cleaner3", BigDecimal.valueOf(50));

        entityManager.persistAndFlush(cleaner1);
        entityManager.persistAndFlush(cleaner2);
        entityManager.persistAndFlush(cleaner3);

        // When
        List<CleanerDetailsEntity> result = cleanerRepository
            .findCleanersByHourlyRateRange(BigDecimal.valueOf(25), BigDecimal.valueOf(40));

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("cleaner2");
    }
}
```

---

## ⚡ Performance & Optimization

### 🚀 Database Performance

#### Connection Pooling

```properties
# application.properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000
```

#### Query Optimization

```java
@Service
public class OptimizedCleanerService {

    // Use @EntityGraph to fetch associations efficiently
    @Query("SELECT c FROM CleanerDetailsEntity c")
    @EntityGraph(attributePaths = {"user", "zones"})
    List<CleanerDetailsEntity> findAllWithDetails();

    // Use pagination for large datasets
    public Page<CleanerCardDto> getCleanersPaginated(Pageable pageable) {
        Page<CleanerDetailsEntity> cleaners = cleanerRepository.findAll(pageable);
        return cleaners.map(cleanerMapper::toCardDto);
    }

    // Cache expensive calculations
    @Cacheable(value = "cleanerRatings", key = "#cleanerId")
    public CleanerRatingDto getCleanerRating(String cleanerId) {
        return calculateRating(cleanerId);
    }
}
```

### 📊 Caching Strategy

```java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES));
        return cacheManager;
    }
}

@Service
public class CachedCleanerService {

    @Cacheable(value = "cleaners", key = "#id")
    public CleanerResponseDto getCleanerById(String id) {
        return cleanerRepository.findById(id)
            .map(cleanerMapper::toResponseDto)
            .orElseThrow(() -> new CleanerNotFoundException("Cleaner not found"));
    }

    @CacheEvict(value = "cleaners", key = "#id")
    public CleanerResponseDto updateCleaner(String id, CleanerUpdateDto dto) {
        // Update logic
    }
}
```

---

## 🚀 Deployment & Environment

### 🌐 Environment Configuration

```yaml
# application-dev.yml
spring:
  profiles: dev
  datasource:
    url: jdbc:postgresql://localhost:5432/cleanme_dev
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update

logging:
  level:
    com.cleanme: DEBUG
    org.springframework.security: DEBUG
```

```yaml
# application-prod.yml
spring:
  profiles: prod
  datasource:
    url: ${DATABASE_URL}
    username: ${JDBC_DB_USERNAME}
    password: ${JDBC_DB_PASSWORD}
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate

logging:
  level:
    root: WARN
    com.cleanme: INFO
```

### 📦 Docker Configuration

```dockerfile
# Dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/cleanme-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

```yaml
# docker-compose.yml
version: "3.8"
services:
  db:
    image: postgres:14
    environment:
      POSTGRES_DB: cleanme
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DATABASE_URL=jdbc:postgresql://db:5432/cleanme
      - JDBC_DB_USERNAME=postgres
      - JDBC_DB_PASSWORD=password
      - JDBC_JW_SECRET=your-secret-key
    depends_on:
      - db

volumes:
  postgres_data:
```

### 🔄 CI/CD Pipeline

```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          java-version: "21"
          distribution: "temurin"

      - name: Run tests
        run: mvn clean test

      - name: Build application
        run: mvn clean package -DskipTests

  deploy:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - name: Deploy to Heroku
        uses: akhileshns/heroku-deploy@v3.12.12
        with:
          heroku_api_key: ${{secrets.HEROKU_API_KEY}}
          heroku_app_name: "cleanme-backend"
          heroku_email: "your-email@example.com"
```

---

## 🛠️ Development Tools

### 📊 API Documentation

```java
@Configuration
@EnableOpenApi
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("CleanMe API")
                .version("1.0")
                .description("REST API for CleanMe cleaning service platform"))
            .addSecurityItem(new SecurityRequirement().addList("JWT"))
            .components(new Components()
                .addSecuritySchemes("JWT", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
}
```

### 🔍 Monitoring & Observability

```properties
# application.properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
management.metrics.export.prometheus.enabled=true
```

```java
@Component
public class CustomMetrics {
    private final MeterRegistry meterRegistry;
    private final Counter reservationCounter;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.reservationCounter = Counter.builder("reservations.created")
            .description("Number of reservations created")
            .register(meterRegistry);
    }

    public void incrementReservationCounter() {
        reservationCounter.increment();
    }
}
```

---

## 📚 Best Practices Summary

### ✅ **Do's**

- Use constructor injection for dependencies
- Implement proper validation with `@Valid`
- Use transactions appropriately (`@Transactional`)
- Follow RESTful API conventions
- Implement comprehensive error handling
- Write unit and integration tests
- Use DTOs for API layer separation
- Implement proper security measures

### ❌ **Don'ts**

- Don't expose entities directly in controllers
- Don't ignore proper exception handling
- Don't skip input validation
- Don't store passwords in plain text
- Don't use `@Autowired` on fields
- Don't forget to handle edge cases
- Don't skip database indexing for performance

---

**Backend Development Guide Complete! 🎯**

_This comprehensive guide covers all aspects of CleanMe backend development with Spring Boot, ensuring clean, maintainable, and scalable code._
