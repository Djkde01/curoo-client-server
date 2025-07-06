#!/bin/bash

# ==============================================
# Production Deployment Script
# ==============================================

set -e

echo "🚀 Deploying Clientback Application - Production Mode"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if .env.prod file exists
if [ ! -f .env.prod ]; then
    print_error ".env.prod file not found!"
    print_warning "Please create .env.prod based on .env.prod.example"
    print_warning "cp .env.prod.example .env.prod"
    print_warning "Then edit .env.prod with your production values"
    exit 1
fi

# Validate required environment variables
print_status "Validating production environment variables..."
source .env.prod

required_vars=(
    "DATABASE_URL"
    "DATABASE_USERNAME" 
    "DATABASE_PASSWORD"
    "JWT_SECRET_KEY"
    "CORS_ALLOWED_ORIGINS"
)

missing_vars=()
for var in "${required_vars[@]}"; do
    if [ -z "${!var}" ]; then
        missing_vars+=("$var")
    fi
done

if [ ${#missing_vars[@]} -ne 0 ]; then
    print_error "Missing required environment variables:"
    for var in "${missing_vars[@]}"; do
        echo "  - $var"
    done
    exit 1
fi

# Validate JWT secret key strength
if [ ${#JWT_SECRET_KEY} -lt 32 ]; then
    print_error "JWT_SECRET_KEY is too short. Must be at least 32 characters."
    exit 1
fi

print_success "Environment validation passed"

# Option to build image or use existing
if [ "$1" = "--build" ]; then
    print_status "Building production Docker image..."
    docker build -t clientback:${APP_VERSION:-latest} .
    print_success "Docker image built successfully"
fi

# Pull any updated base images if not building
if [ "$1" != "--build" ]; then
    print_status "Pulling latest base images..."
    docker-compose -f docker-compose.prod.yml pull
fi

# Start production services
print_status "Starting production services..."
docker-compose -f docker-compose.prod.yml --env-file .env.prod up -d

# Wait for services to be healthy
print_status "Waiting for services to be healthy..."
sleep 10

# Check application health
print_status "Checking application health..."
for i in {1..30}; do
    if curl -f http://localhost:${SERVER_PORT:-8080}/api/actuator/health >/dev/null 2>&1; then
        print_success "Application is healthy and running!"
        break
    fi
    if [ $i -eq 30 ]; then
        print_error "Application failed to start or become healthy"
        docker-compose -f docker-compose.prod.yml logs app
        exit 1
    fi
    sleep 2
done

# Display running services
print_status "Production deployment completed successfully!"
echo ""
echo "Running services:"
docker-compose -f docker-compose.prod.yml ps

echo ""
echo "Application URLs:"
echo "  Health Check: http://localhost:${SERVER_PORT:-8080}/api/actuator/health"
echo "  Metrics: http://localhost:${SERVER_PORT:-8080}/api/actuator/metrics"
echo ""
echo "To view logs: docker-compose -f docker-compose.prod.yml logs -f"
echo "To stop: docker-compose -f docker-compose.prod.yml down"
