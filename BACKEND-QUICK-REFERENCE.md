# 🚀 CleanMe Backend - Quick Reference

> **Essential commands, patterns, and shortcuts for CleanMe Spring Boot developers**

## ⚡ Quick Commands

```bash
# Development
mvn spring-boot:run              # Start dev server (localhost:8080)
mvn clean install               # Build and install dependencies
mvn test                        # Run unit tests
mvn spring-boot:build-image     # Build Docker image

# Advanced
mvn clean package -DskipTests   # Build without tests
mvn dependency:tree             # View dependency tree
mvn spring-boot:run -Dspring.profiles.active=dev  # Run with profile
curl http://localhost:8080/actuator/health        # Health check
```

## 🏗️ Project Structure

```
src/main/java/com/cleanme/
├── 🎮 controller/       # REST endpoints (@RestController)
├── 💼 service/          # Business logic (@Service)
├── 🗃️ repository/       # Data access (@Repository)
├── 🏢 entity/           # JPA entities (@Entity)
├── 📦 dto/              # Data transfer objects
├── 🔒 security/         # Security config & JWT
├── ⚙️ configuration/    # App configuration
├── 🏷️ enums/            # Enumerations
└── ❌ exception/        # Exception handling
```

## 🔧 Essential Patterns

### Controller Pattern

```java
@RestController
@RequestMapping("/api/cleaners")
public class CleanerController {
    private final CleanerService cleanerService;

    public CleanerController(CleanerService cleanerService) {
        this.cleanerService = cleanerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CleanerResponseDto> getCleaner(@PathVariable String id) {
        CleanerResponseDto cleaner = cleanerService.getCleanerById(id);
        return ResponseEntity.ok(cleaner);
    }

    @PostMapping
    public ResponseEntity<CleanerResponseDto> createCleaner(@Valid @RequestBody CleanerCreateDto dto) {
        CleanerResponseDto created = cleanerService.createCleaner(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
```

### Service Pattern

```java
@Service
@Transactional
public class CleanerService {
    private final CleanerRepository cleanerRepository;

    public CleanerService(CleanerRepository cleanerRepository) {
        this.cleanerRepository = cleanerRepository;
    }

    @Transactional(readOnly = true)
    public CleanerResponseDto getCleanerById(String id) {
        return cleanerRepository.findById(id)
            .map(cleanerMapper::toResponseDto)
            .orElseThrow(() -> new CleanerNotFoundException("Cleaner not found"));
    }

    @Transactional
    public CleanerResponseDto createCleaner(CleanerCreateDto dto) {
        CleanerDetailsEntity entity = cleanerMapper.toEntity(dto);
        CleanerDetailsEntity saved = cleanerRepository.save(entity);
        return cleanerMapper.toResponseDto(saved);
    }
}
```

### Repository Pattern

```java
@Repository
public interface CleanerRepository extends JpaRepository<CleanerDetailsEntity, String> {

    List<CleanerDetailsEntity> findByHourlyRateBetween(BigDecimal min, BigDecimal max);

    @Query("SELECT c FROM CleanerDetailsEntity c WHERE c.user.userType = :userType")
    List<CleanerDetailsEntity> findByUserType(@Param("userType") UserType userType);

    @Query(value = "SELECT * FROM cleaner_details WHERE hourly_rate > ?1", nativeQuery = true)
    List<CleanerDetailsEntity> findExpensiveCleaners(BigDecimal rate);
}
```

## 🗄️ Entity Patterns

### Basic Entity

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

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

### Relationships

```java
// One-to-One
@OneToOne
@JoinColumn(name = "user_id")
private UsersEntity user;

// One-to-Many
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
private List<ReservationEntity> reservations;

// Many-to-One
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "cleaner_id")
private CleanerDetailsEntity cleaner;
```

## 🔒 Security Patterns

### JWT Authentication

```java
@Component
public class JwtUtils {
    private final String secret = "your-secret-key";
    private final int expiration = 86400000; // 24 hours

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
```

### Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

## 📡 API Response Patterns

### Success Responses

```java
// 200 OK - Success
return ResponseEntity.ok(data);

// 201 Created - Resource created
return ResponseEntity.status(HttpStatus.CREATED).body(data);

// 204 No Content - Success with no body
return ResponseEntity.noContent().build();
```

### Error Responses

```java
// 400 Bad Request - Client error
return ResponseEntity.badRequest().build();

// 404 Not Found - Resource not found
return ResponseEntity.notFound().build();

// 500 Internal Server Error - Server error
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
```

### Exception Handling

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CleanerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCleanerNotFound(CleanerNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("CLEANER_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
```

## 🧪 Testing Shortcuts

### Unit Test Template

```java
@ExtendWith(MockitoExtension.class)
class CleanerServiceTest {

    @Mock
    private CleanerRepository cleanerRepository;

    @InjectMocks
    private CleanerService cleanerService;

    @Test
    void getCleaner_WhenExists_ShouldReturnCleaner() {
        // Given
        String cleanerId = "test-id";
        CleanerDetailsEntity mockCleaner = createMockCleaner();
        when(cleanerRepository.findById(cleanerId)).thenReturn(Optional.of(mockCleaner));

        // When
        CleanerResponseDto result = cleanerService.getCleanerById(cleanerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cleanerId);
    }
}
```

### Integration Test Template

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class CleanerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createCleaner_ShouldReturnCreated() {
        CleanerCreateDto dto = new CleanerCreateDto("Bio", BigDecimal.valueOf(25));

        ResponseEntity<CleanerResponseDto> response = restTemplate.postForEntity(
            "/api/cleaners", dto, CleanerResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
```

## 🗃️ Database Shortcuts

### Query Methods

```java
// Find by field
findByFirstName(String firstName)
findByEmailAndUserType(String email, UserType type)

// Comparison
findByHourlyRateGreaterThan(BigDecimal rate)
findByCreatedAtBetween(LocalDateTime start, LocalDateTime end)

// Collections
findByZonesContaining(String zone)
findByServicesOfferedIn(List<String> services)

// Sorting and Pagination
findAllByOrderByCreatedAtDesc()
findAll(Pageable pageable)
```

### Custom Queries

```java
@Query("SELECT c FROM CleanerDetailsEntity c WHERE c.hourlyRate BETWEEN :min AND :max")
List<CleanerDetailsEntity> findByRateRange(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

@Query(value = "SELECT COUNT(*) FROM reservations WHERE cleaner_id = ?1", nativeQuery = true)
Long countReservationsByCleanerId(String cleanerId);

@Modifying
@Query("UPDATE UsersEntity u SET u.firstName = :name WHERE u.id = :id")
void updateUserName(@Param("id") String id, @Param("name") String name);
```

## ⚙️ Configuration Shortcuts

### Application Properties

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/cleanme
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secret=your-secret-key
jwt.expiration=86400000

# Logging
logging.level.com.cleanme=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Profile-specific Properties

```yaml
# application-dev.yml
spring:
  profiles: dev
  jpa:
    show-sql: true
  logging:
    level:
      com.cleanme: DEBUG

# application-prod.yml
spring:
  profiles: prod
  jpa:
    show-sql: false
  logging:
    level:
      root: WARN
```

## 🔄 Data Mapping

### DTO Mapping

```java
@Component
public class CleanerMapper {

    public CleanerResponseDto toResponseDto(CleanerDetailsEntity entity) {
        return CleanerResponseDto.builder()
            .id(entity.getId())
            .fullName(entity.getUser().getFirstName() + " " + entity.getUser().getLastName())
            .bio(entity.getBio())
            .hourlyRate(entity.getHourlyRate())
            .build();
    }

    public CleanerDetailsEntity toEntity(CleanerCreateDto dto) {
        CleanerDetailsEntity entity = new CleanerDetailsEntity();
        entity.setBio(dto.getBio());
        entity.setHourlyRate(dto.getHourlyRate());
        return entity;
    }
}
```

## 📊 Validation Patterns

### Request Validation

```java
public class CleanerCreateDto {
    @NotBlank(message = "Bio is required")
    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    @NotNull(message = "Hourly rate is required")
    @DecimalMin(value = "5.0", message = "Hourly rate must be at least 5")
    @DecimalMax(value = "200.0", message = "Hourly rate must not exceed 200")
    private BigDecimal hourlyRate;

    @Email(message = "Invalid email format")
    private String email;
}
```

### Custom Validation

```java
@Component
public class CleanerValidator {

    public void validateCleanerData(CleanerCreateDto dto) {
        if (dto.getHourlyRate().compareTo(BigDecimal.valueOf(5)) < 0) {
            throw new ValidationException("Hourly rate too low");
        }

        if (dto.getBio() != null && dto.getBio().length() > 1000) {
            throw new ValidationException("Bio too long");
        }
    }
}
```

## 🔍 Common Debugging

### Logging Patterns

```java
@Slf4j
@Service
public class CleanerService {

    public CleanerResponseDto getCleaner(String id) {
        log.debug("Fetching cleaner with id: {}", id);

        try {
            CleanerDetailsEntity cleaner = cleanerRepository.findById(id)
                .orElseThrow(() -> new CleanerNotFoundException("Cleaner not found"));

            log.info("Successfully retrieved cleaner: {}", cleaner.getId());
            return cleanerMapper.toResponseDto(cleaner);

        } catch (Exception e) {
            log.error("Error fetching cleaner with id: {}", id, e);
            throw e;
        }
    }
}
```

### Health Checks

```bash
# Application health
curl http://localhost:8080/actuator/health

# Database connectivity
curl http://localhost:8080/actuator/health/db

# Custom health check
curl http://localhost:8080/actuator/health/custom
```

## 🚀 Performance Tips

### Optimization Checklist

- ✅ Use `@Transactional(readOnly = true)` for read operations
- ✅ Implement pagination for large datasets
- ✅ Use `@EntityGraph` to avoid N+1 queries
- ✅ Add database indexes for frequently queried fields
- ✅ Cache expensive operations with `@Cacheable`
- ✅ Use connection pooling (HikariCP)

### Query Optimization

```java
// Use JOIN FETCH to avoid N+1 problem
@Query("SELECT c FROM CleanerDetailsEntity c JOIN FETCH c.user WHERE c.id = :id")
Optional<CleanerDetailsEntity> findByIdWithUser(@Param("id") String id);

// Use pagination for large results
Page<CleanerDetailsEntity> findAll(Pageable pageable);

// Use projections for specific fields
@Query("SELECT c.id, c.bio FROM CleanerDetailsEntity c")
List<Object[]> findCleanerSummary();
```

## 📚 Key Dependencies

```xml
<!-- Core Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Database -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>

<!-- Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
</dependency>

<!-- Utils -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

## 🌐 Environment Variables

```bash
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/cleanme
JDBC_DB_USERNAME=postgres
JDBC_DB_PASSWORD=your_password

# Security
JDBC_JW_SECRET=your-secret-key-256-bits-minimum

# Application
FRONTEND_URL=http://localhost:4200
PORT=8080
```

## 🔗 Useful Endpoints

```bash
# Health & Monitoring
GET /actuator/health           # Application health
GET /actuator/info             # Application info
GET /actuator/metrics          # Application metrics

# Authentication
POST /api/auth/register        # User registration
POST /api/auth/login           # User login

# Core Resources
GET /api/cleaners              # Get all cleaners
GET /api/cleaners/{id}         # Get specific cleaner
POST /api/reservations         # Create reservation
GET /api/reservations          # Get reservations
```

---

**Keep this handy for daily Spring Boot development! 📌**
