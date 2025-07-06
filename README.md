# 🏢 Clientback - Enterprise Client Management API

A secure, production-ready Spring Boot REST API for managing client records with user authentication and authorization. Built with modern technologies and enterprise-grade security practices.

## 📋 Project Description

**Clientback** is a comprehensive client management system that provides:

- **User Authentication & Authorization**: JWT-based secure authentication system
- **Client Management**: Full CRUD operations for client records with user-specific data isolation
- **RESTful API**: Well-documented API endpoints following REST principles
- **Production-Ready**: Complete containerization, monitoring, and deployment automation
- **Security-First**: User data isolation, encrypted passwords, and secure JWT handling
- **Monitoring & Health Checks**: Built-in health endpoints and metrics for operational excellence

### Key Features

- ✅ **User Registration & Login** with BCrypt password encryption
- ✅ **JWT Authentication** for stateless, scalable security
- ✅ **User-Specific Client Management** - each user only sees their own clients
- ✅ **Complete CRUD Operations** for both users and clients
- ✅ **Input Validation** and error handling
- ✅ **Database Integration** with PostgreSQL and JPA/Hibernate
- ✅ **API Documentation** with OpenAPI 3.0/Swagger
- ✅ **Health Monitoring** with Spring Boot Actuator
- ✅ **Containerized Deployment** with Docker and Docker Compose
- ✅ **Environment-Driven Configuration** for different deployment stages

## 🛠 Technologies & Tools Used

### Backend Framework

- **Java 17** - Latest LTS version with modern language features
- **Spring Boot 3.x** - Modern Spring framework with auto-configuration
- **Spring Security** - Comprehensive security framework with JWT support
- **Spring Data JPA** - Data access layer with Hibernate ORM
- **Spring Boot Actuator** - Production monitoring and health checks

### Database

- **PostgreSQL 15** - Robust, ACID-compliant relational database
- **HikariCP** - High-performance connection pooling
- **Flyway/Liquibase Ready** - Database migration support

### Security

- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password hashing algorithm
- **CORS** - Cross-Origin Resource Sharing configuration

### API Documentation

- **OpenAPI 3.0** - API specification standard
- **Swagger UI** - Interactive API documentation (dev environment)

### Build & Dependency Management

- **Gradle 8.5** - Modern build automation tool
- **Gradle Wrapper** - Ensures consistent build environment

### Containerization & Deployment

- **Docker** - Container runtime and image building
- **Docker Compose** - Multi-container application orchestration
- **Multi-stage Builds** - Optimized container images

### Development & Testing Tools

- **Postman** - API testing and documentation
- **Git** - Version control system

### Monitoring & Operations

- **Spring Boot Actuator** - Health checks and metrics
- **Micrometer** - Application metrics collection
- **Structured Logging** - JSON formatted logs for analysis

## 🚀 Quick Start Guide

### Prerequisites

Before running the project, ensure you have the following installed:

- **Docker** (v20.10+) and **Docker Compose** (v2.0+)
- **Git** for cloning the repository
- **Java 17** (optional, for local development without Docker)
- **Gradle 8.5+** (optional, for local development without Docker)

### 1. Clone the Repository

```bash
git clone https://github.com/Djkde01/curoo-client-server/
cd curoo-client-server
```

### 2. Development Setup (Recommended)

#### Quick Start with Docker Compose

```bash
# Copy development environment template
cp .env.example .env

# Edit .env with your preferred settings (optional - defaults work fine)
nano .env

# Start the complete development stack
./scripts/dev-start.sh
```

This script will:

- Start PostgreSQL database container
- Build and start the application container
- Initialize the database schema
- Verify the application health

#### Manual Development Setup

```bash
# 1. Start PostgreSQL only
docker-compose up -d db

# 2. Initialize database schema
./scripts/init-db.sh

# 3. Build and run the application
./gradlew bootRun
```

### 3. Verify Installation

Once the application is running, verify the setup:

```bash
# Check application health
./scripts/health-check.sh

# Or manually check endpoints
curl http://localhost:8080/api/actuator/health
curl http://localhost:8080/api/v3/api-docs
```

### 4. Access the Application

- **API Base URL**: `http://localhost:8080/api`
- **Health Check**: `http://localhost:8080/api/actuator/health`
- **API Documentation**: `http://localhost:8080/api/swagger-ui.html` (development only)
- **OpenAPI Spec**: `http://localhost:8080/api/v3/api-docs`

## 📦 Dependencies & Requirements

### System Dependencies

| Component          | Version | Purpose                       |
| ------------------ | ------- | ----------------------------- |
| **Docker**         | 20.10+  | Container runtime             |
| **Docker Compose** | 2.0+    | Multi-container orchestration |
| **PostgreSQL**     | 15+     | Primary database              |
| **Java**           | 17 LTS  | Application runtime           |

### Application Dependencies (Managed by Gradle)

```gradle
dependencies {
    // Spring Boot Starters
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Database
    runtimeOnly 'org.postgresql:postgresql'

    // JWT Support
    implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'

    // API Documentation
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.0'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
}
```

### Environment Configuration

The application uses environment variables for configuration. Key variables include:

```bash
# Application
SPRING_PROFILES_ACTIVE=dev          # Application profile (dev/prod)
SERVER_PORT=8080                    # Application port

# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/curooclientback
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=super-secure-password

# Security
JWT_SECRET_KEY=your-256-bit-secret-key
JWT_EXPIRATION_MS=3600000          # 1 hour

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:3000
```

## 🗄️ Database Setup & Schema Creation

### Automatic Schema Initialization

The project includes automated database schema creation:

```bash
# Initialize database schema (automatically called by dev-start.sh)
./scripts/init-db.sh

# For production environment
ENVIRONMENT=prod ./scripts/init-db.sh
```

### Manual Schema Creation

If you prefer manual setup or need to understand the schema:

#### 1. Connect to PostgreSQL

```bash
# Using Docker Compose
docker-compose exec db psql -U postgres -d curooclientback

# Or using local PostgreSQL
psql -U postgres -d curooclientback
```

#### 2. Execute Schema Script

```sql
-- Run the complete schema script
\i scripts/schemas.sql
```

### Database Schema Overview

The application uses a simple but robust schema:

#### Users Table

```sql
CREATE TABLE Users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    mobile_phone VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Clients Table

```sql
CREATE TABLE Clients (
    client_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES Users(user_id),
    id_type VARCHAR(50) NOT NULL,
    id_number VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Key Features

- **Foreign Key Relationship**: Clients are linked to Users via `user_id`
- **Unique Constraints**: Email, mobile phone, and ID numbers are unique
- **Automatic Timestamps**: Creation and modification dates are automatically managed
- **Data Integrity**: Referential integrity enforced at database level

### Database Migrations

For production deployments, consider using database migration tools:

```bash
# Backup before migrations
./scripts/backup-db.sh

# Apply migrations (when implemented)
./gradlew flywayMigrate
```

## 🔐 Authentication & Authorization

### JWT Authentication Flow

1. **User Registration**: `POST /api/users/register`
2. **User Login**: `POST /api/users/login` → Returns JWT token
3. **Authenticated Requests**: Include `Authorization: Bearer <token>` header
4. **Token Expiration**: Tokens expire after configured time (default: 1 hour)

### API Endpoints

#### User Management

```http
POST /api/users/register    # Create new user account
POST /api/users/login       # Authenticate and get JWT token
PUT  /api/users/{userId}    # Update user profile (requires auth)
```

#### Client Management (All require authentication)

```http
GET    /api/clients/all                    # Get all user's clients
GET    /api/clients/{idType}/{idNumber}    # Get specific client
POST   /api/clients/save                   # Create new client
PUT    /api/clients/{clientId}             # Update existing client
DELETE /api/clients/{clientId}             # Delete client
```

#### Health & Monitoring

```http
GET /api/actuator/health     # Application health status
GET /api/actuator/info       # Application information
GET /api/actuator/metrics    # Application metrics
```

## 🧪 Testing the API

### Using Postman

1. **Import Collection**: Use `Curoo_Clientback_API.postman_collection.json`
2. **Import Environment**: Use `postman-environments/development.postman_environment.json`
3. **Test Workflow**:
   - Register a new user
   - Login to get JWT token (auto-saved)
   - Create, read, update, delete clients

### Using cURL

```bash
# Register user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "name": "Test",
    "surname": "User",
    "mobilePhone": "1234567890"
  }'

# Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=test@example.com&password=password123"

# Create client (replace TOKEN with actual JWT)
curl -X POST http://localhost:8080/api/clients/save \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "idType": "CC",
    "idNumber": "1234567890",
    "name": "John",
    "surname": "Doe"
  }'
```

## 🚀 Production Deployment

### Environment Setup

```bash
# 1. Copy production template
cp .env.prod.example .env.prod

# 2. Configure production values
nano .env.prod
```

### Required Production Configuration

```bash
# Database (use external database recommended)
DATABASE_URL=jdbc:postgresql://prod-db-host:5432/clientback
DATABASE_USERNAME=app_user
DATABASE_PASSWORD=super_secure_production_password

# Security (generate new secret)
JWT_SECRET_KEY=$(openssl rand -hex 32)

# CORS (your actual domain)
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://api.yourdomain.com
```

### Deploy to Production

```bash
# Build and deploy
./scripts/prod-deploy.sh --build

# Verify deployment
ENVIRONMENT=prod ./scripts/health-check.sh

# Setup automated backups
crontab -e
# Add: 0 2 * * * /path/to/clientback/scripts/backup-db.sh
```

## 📊 Monitoring & Operations

### Health Checks

```bash
# Application health
curl http://localhost:8080/api/actuator/health

# Detailed health with authentication
curl -H "Authorization: Bearer TOKEN" \
     http://localhost:8080/api/actuator/health
```

### Metrics & Monitoring

```bash
# Application metrics
curl http://localhost:8080/api/actuator/metrics

# Specific metric
curl http://localhost:8080/api/actuator/metrics/jvm.memory.used
```

### Database Operations

```bash
# Create backup
./scripts/backup-db.sh

# Production backup
ENVIRONMENT=prod ./scripts/backup-db.sh

# Restore backup (example)
gunzip -c backup/clientback_backup_dev_20250105_120000.sql.gz | \
  docker-compose exec -T db psql -U postgres -d curooclientback
```

## 🛠 Development Guidelines

### Code Structure

```
src/main/java/com/curootest/clientback/
├── domain/           # DTOs and business logic interfaces
├── persistence/      # JPA entities and repositories
└── web/controller/   # REST controllers and API endpoints
```

### Configuration Profiles

- **dev**: Development with verbose logging and Swagger enabled
- **prod**: Production with optimized settings and security hardening

### Environment Variables

All configuration uses environment variables for:

- ✅ Database connections
- ✅ Security settings (JWT secrets, CORS)
- ✅ Application behavior (logging, monitoring)
- ✅ Feature toggles (Swagger, metrics access)

## 🔧 Troubleshooting

### Common Issues

1. **Port 8080 already in use**

   ```bash
   # Change port in .env file
   SERVER_PORT=8081
   ```

2. **Database connection refused**

   ```bash
   # Ensure PostgreSQL is running
   docker-compose up -d db
   ```

3. **JWT token expired**

   ```bash
   # Re-login to get new token
   curl -X POST http://localhost:8080/api/users/login \
     -d "email=user@example.com&password=password"
   ```

4. **Permission denied on scripts**
   ```bash
   chmod +x scripts/*.sh
   ```

### Useful Commands

```bash
# View application logs
docker-compose logs -f app

# Access database directly
docker-compose exec db psql -U postgres -d curooclientback

# Restart application only
docker-compose restart app

# Clean restart (removes volumes - ⚠️ DATA LOSS)
docker-compose down -v && docker-compose up -d
```

## 📚 Additional Resources

- **[Production Deployment Guide](PRODUCTION-DEPLOYMENT.md)** - Comprehensive production setup
- **[Deployment Summary](DEPLOYMENT-SUMMARY.md)** - Complete deployment checklist
- **[Postman Collection Guide](postman-collection-README.md)** - API testing documentation
- **[Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)**
- **[Spring Security Documentation](https://docs.spring.io/spring-security/reference/)**
- **[PostgreSQL Documentation](https://www.postgresql.org/docs/)**

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🎉 Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community for the robust database
- Docker team for containerization technology
- All contributors and users of this project

---

**🚀 Ready to build secure, scalable client management systems!**
