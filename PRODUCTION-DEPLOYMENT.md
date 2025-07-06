# Clientback Production Deployment Guide

This guide covers deploying the Clientback Spring Boot application in production using Docker and environment variables.

## 🚀 Quick Start

### Prerequisites

- Docker & Docker Compose
- Git
- A PostgreSQL database (external or Docker)

### 1. Clone and Setup

```bash
git clone <your-repo>
cd clientback
```

### 2. Configure Production Environment

```bash
# Copy production environment template
cp .env.prod.example .env.prod

# Edit with your production values
nano .env.prod
```

### 3. Deploy

```bash
# Deploy with building image locally
./scripts/prod-deploy.sh --build

# Or deploy using pre-built image
./scripts/prod-deploy.sh
```

## 📋 Environment Configuration

### Required Production Variables

These variables **must** be set in `.env.prod`:

```bash
# Database (REQUIRED)
DATABASE_URL=jdbc:postgresql://your-db-host:5432/your-db-name
DATABASE_USERNAME=your-db-username
DATABASE_PASSWORD=your-secure-db-password

# Security (REQUIRED)
JWT_SECRET_KEY=your-256-bit-secret-key
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://www.yourdomain.com
```

### Optional Production Variables

```bash
# Application
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod

# Database Connection Pool
DB_POOL_SIZE=25
DB_MIN_IDLE=15

# JWT
JWT_EXPIRATION_MS=7200000

# Logging
LOG_LEVEL=INFO
SECURITY_LOG_LEVEL=WARN

# Monitoring
ACTUATOR_ENDPOINTS=health,info,metrics,prometheus
METRICS_ENABLED=true

# Docker
DOCKER_REGISTRY=your-registry.com
APP_VERSION=latest
```

## 🛠 Deployment Options

### Option 1: Local Docker Build

```bash
# Build and deploy locally
./scripts/prod-deploy.sh --build
```

### Option 2: Pre-built Image

```bash
# Set image in .env.prod
DOCKER_REGISTRY=your-registry.com
APP_VERSION=v1.0.0

# Deploy
./scripts/prod-deploy.sh
```

### Option 3: External Database

```bash
# Configure external database in .env.prod
DATABASE_URL=jdbc:postgresql://external-db:5432/clientback
DATABASE_USERNAME=app_user
DATABASE_PASSWORD=secure_password

# Deploy without database container
docker-compose -f docker-compose.prod.yml --env-file .env.prod up -d app
```

## 🔧 Production Configuration Details

### Security Considerations

1. **JWT Secret**: Generate a secure 256-bit key:

   ```bash
   openssl rand -hex 32
   ```

2. **Database Password**: Use a strong, unique password

3. **CORS Origins**: Set only your actual domain(s)

4. **Firewall**: Ensure only necessary ports are exposed

### Database Configuration

- **Connection Pool**: Optimized for production with larger pool sizes
- **DDL Auto**: Set to `validate` to prevent schema changes
- **Show SQL**: Disabled for performance

### Logging Configuration

- **Application Logs**: INFO level for production
- **Security Logs**: WARN level to reduce noise
- **File Rotation**: JSON format with size limits

### Monitoring

- **Health Checks**: Available at `/api/actuator/health`
- **Metrics**: Available at `/api/actuator/metrics`
- **Prometheus**: Available at `/api/actuator/prometheus`

## 📊 Monitoring and Health Checks

### Health Check Script

```bash
# Check application health
./scripts/health-check.sh

# Check with custom host/port
HOST=myserver.com PORT=8080 ./scripts/health-check.sh

# Production environment check
ENVIRONMENT=prod ./scripts/health-check.sh
```

### Available Endpoints

- Health: `http://your-server:8080/api/actuator/health`
- Info: `http://your-server:8080/api/actuator/info`
- Metrics: `http://your-server:8080/api/actuator/metrics`

## 💾 Database Management

### Backup

```bash
# Create backup
./scripts/backup-db.sh

# Production backup
ENVIRONMENT=prod ./scripts/backup-db.sh

# Custom backup location
BACKUP_DIR=/path/to/backups ./scripts/backup-db.sh
```

### Restore

```bash
# Stop application
docker-compose -f docker-compose.prod.yml down app

# Restore backup
gunzip -c backup/clientback_backup_prod_20250105_120000.sql.gz | \
  docker-compose -f docker-compose.prod.yml exec -T db psql -U $DATABASE_USERNAME -d $POSTGRES_DB

# Start application
docker-compose -f docker-compose.prod.yml up -d app
```

## 🔄 CI/CD Integration

### GitHub Actions Example

```yaml
name: Deploy to Production

on:
  push:
    tags: ["v*"]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Build and push Docker image
        run: |
          docker build -t myregistry/clientback:${{ github.ref_name }} .
          docker push myregistry/clientback:${{ github.ref_name }}

      - name: Deploy to production
        run: |
          echo "DATABASE_URL=${{ secrets.DATABASE_URL }}" > .env.prod
          echo "DATABASE_USERNAME=${{ secrets.DATABASE_USERNAME }}" >> .env.prod
          echo "DATABASE_PASSWORD=${{ secrets.DATABASE_PASSWORD }}" >> .env.prod
          echo "JWT_SECRET_KEY=${{ secrets.JWT_SECRET_KEY }}" >> .env.prod
          echo "CORS_ALLOWED_ORIGINS=${{ secrets.CORS_ALLOWED_ORIGINS }}" >> .env.prod
          echo "APP_VERSION=${{ github.ref_name }}" >> .env.prod
          ./scripts/prod-deploy.sh
```

## 🐛 Troubleshooting

### Common Issues

1. **Application won't start**

   ```bash
   # Check logs
   docker-compose -f docker-compose.prod.yml logs app

   # Check health
   ./scripts/health-check.sh
   ```

2. **Database connection issues**

   ```bash
   # Test database connectivity
   docker-compose -f docker-compose.prod.yml exec db pg_isready -U $DATABASE_USERNAME
   ```

3. **Permission denied errors**
   ```bash
   # Make scripts executable
   chmod +x scripts/*.sh
   ```

### Useful Commands

```bash
# View all services
docker-compose -f docker-compose.prod.yml ps

# Follow logs
docker-compose -f docker-compose.prod.yml logs -f

# Restart application
docker-compose -f docker-compose.prod.yml restart app

# Update application
docker-compose -f docker-compose.prod.yml pull app
docker-compose -f docker-compose.prod.yml up -d app

# Stop all services
docker-compose -f docker-compose.prod.yml down

# Stop and remove volumes (⚠️ DATA LOSS)
docker-compose -f docker-compose.prod.yml down -v
```

## 🔒 Security Checklist

- [ ] Strong JWT secret key (256-bit)
- [ ] Secure database password
- [ ] CORS origins restricted to your domains
- [ ] Swagger/OpenAPI disabled in production
- [ ] Database DDL auto set to `validate`
- [ ] HTTPS configured (via reverse proxy)
- [ ] Firewall configured
- [ ] Regular security updates
- [ ] Database backups automated
- [ ] Log monitoring in place

## 📚 Additional Resources

- [Spring Boot Production Features](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Docker Best Practices](https://docs.docker.com/develop/best-practices/)
- [PostgreSQL Security](https://www.postgresql.org/docs/current/security.html)
