#!/bin/bash

# ==============================================
# Development Build and Run Script
# ==============================================

set -e

echo "🚀 Starting Clientback Application - Development Mode"

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

# Check if .env file exists
if [ ! -f .env ]; then
    print_warning ".env file not found. Copying from .env.example"
    cp .env.example .env
    print_warning "Please review and update .env file with your configuration"
fi

# Load environment variables
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
    print_status "Environment variables loaded from .env"
fi

# Build the application
print_status "Building application..."
./gradlew clean build -x test

if [ $? -eq 0 ]; then
    print_success "Application built successfully"
else
    print_error "Build failed"
    exit 1
fi

# Build Docker image
print_status "Building Docker image..."
docker build -t clientback:dev .

if [ $? -eq 0 ]; then
    print_success "Docker image built successfully"
else
    print_error "Docker build failed"
    exit 1
fi

# Start services with Docker Compose
print_status "Starting services with Docker Compose..."
docker compose up -d

if [ $? -eq 0 ]; then
    print_success "Services started successfully"
    echo ""
    print_status "🎯 Application is running at: http://localhost:${SERVER_PORT:-8080}/api"
    print_status "📚 Swagger UI available at: http://localhost:${SERVER_PORT:-8080}/api/swagger-ui.html"
    print_status "🏥 Health check at: http://localhost:${SERVER_PORT:-8080}/api/actuator/health"
    echo ""
    print_status "To view logs: docker compose logs -f app"
    print_status "To stop services: docker compose down"
else
    print_error "Failed to start services"
    exit 1
fi
