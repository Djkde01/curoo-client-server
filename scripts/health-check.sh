#!/bin/bash

# ==============================================
# Application Health Check Script
# ==============================================

set -e

# Configuration
HOST=${HOST:-localhost}
PORT=${PORT:-8080}
TIMEOUT=${TIMEOUT:-10}
ENVIRONMENT=${ENVIRONMENT:-dev}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

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

# Function to check endpoint
check_endpoint() {
    local endpoint=$1
    local description=$2
    local expected_status=${3:-200}
    
    print_status "Checking $description..."
    
    response=$(curl -s -o /dev/null -w "%{http_code}" --connect-timeout $TIMEOUT "http://$HOST:$PORT$endpoint" || echo "000")
    
    if [ "$response" -eq "$expected_status" ]; then
        print_success "$description is healthy (HTTP $response)"
        return 0
    else
        print_error "$description failed (HTTP $response, expected $expected_status)"
        return 1
    fi
}

# Function to check detailed health
check_detailed_health() {
    print_status "Getting detailed health information..."
    
    health_response=$(curl -s --connect-timeout $TIMEOUT "http://$HOST:$PORT/api/actuator/health" || echo "{}")
    
    if [ "$health_response" != "{}" ]; then
        echo "Health Details:"
        echo "$health_response" | python3 -m json.tool 2>/dev/null || echo "$health_response"
        
        # Check if overall status is UP
        status=$(echo "$health_response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        if [ "$status" = "UP" ]; then
            print_success "Overall application status: UP"
            return 0
        else
            print_error "Overall application status: $status"
            return 1
        fi
    else
        print_error "Could not retrieve health information"
        return 1
    fi
}

# Main health check
echo "🏥 Clientback Application Health Check"
echo "======================================"
echo "Environment: $ENVIRONMENT"
echo "Target: http://$HOST:$PORT"
echo "Timeout: ${TIMEOUT}s"
echo ""

failed_checks=0

# Basic connectivity
if ! check_endpoint "/api/actuator/health" "Basic health endpoint"; then
    ((failed_checks++))
fi

# Detailed health check
if ! check_detailed_health; then
    ((failed_checks++))
fi

# Info endpoint
if ! check_endpoint "/api/actuator/info" "Application info endpoint"; then
    ((failed_checks++))
fi

# Metrics endpoint (if enabled)
if [ "$ENVIRONMENT" = "prod" ]; then
    if ! check_endpoint "/api/actuator/metrics" "Metrics endpoint"; then
        print_warning "Metrics endpoint not accessible (may be restricted)"
    fi
fi

# API endpoints (basic structure check)
if ! check_endpoint "/api/v3/api-docs" "OpenAPI documentation" 200; then
    if [ "$ENVIRONMENT" = "dev" ]; then
        ((failed_checks++))
    else
        print_warning "OpenAPI docs disabled in production (expected)"
    fi
fi

echo ""
echo "======================================"

if [ $failed_checks -eq 0 ]; then
    print_success "All health checks passed! Application is healthy."
    exit 0
else
    print_error "$failed_checks health check(s) failed!"
    
    if [ "$ENVIRONMENT" = "prod" ]; then
        echo ""
        echo "Troubleshooting suggestions:"
        echo "1. Check application logs: docker-compose -f docker-compose.prod.yml logs app"
        echo "2. Verify database connectivity"
        echo "3. Check environment variables configuration"
        echo "4. Ensure all required services are running"
    fi
    
    exit 1
fi
