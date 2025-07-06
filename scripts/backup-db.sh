#!/bin/bash

# ==============================================
# Database Backup Script
# ==============================================

set -e

# Configuration
BACKUP_DIR=${BACKUP_DIR:-./backup}
RETENTION_DAYS=${RETENTION_DAYS:-7}
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

# Load environment variables based on environment
if [ "$ENVIRONMENT" = "prod" ]; then
    if [ -f .env.prod ]; then
        source .env.prod
    else
        print_error ".env.prod file not found!"
        exit 1
    fi
else
    if [ -f .env ]; then
        source .env
    else
        print_error ".env file not found!"
        exit 1
    fi
fi

# Create backup directory
mkdir -p "$BACKUP_DIR"

# Generate backup filename with timestamp
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/clientback_backup_${ENVIRONMENT}_${TIMESTAMP}.sql"

echo "📦 Database Backup - $ENVIRONMENT Environment"
echo "=============================================="
echo "Backup file: $BACKUP_FILE"
echo ""

# Check if database container is running
if [ "$ENVIRONMENT" = "prod" ]; then
    COMPOSE_FILE="docker-compose.prod.yml"
else
    COMPOSE_FILE="docker-compose.yml"
fi

if ! docker-compose -f "$COMPOSE_FILE" ps db | grep -q "Up"; then
    print_error "Database container is not running!"
    print_status "Start the database with: docker-compose -f $COMPOSE_FILE up -d db"
    exit 1
fi

# Perform backup
print_status "Creating database backup..."

if docker-compose -f "$COMPOSE_FILE" exec -T db pg_dump \
    -U "$DATABASE_USERNAME" \
    -d "${POSTGRES_DB:-$(basename "$DATABASE_URL")}" \
    --no-password \
    --verbose \
    --clean \
    --if-exists \
    --create > "$BACKUP_FILE"; then
    
    print_success "Backup created successfully: $BACKUP_FILE"
    
    # Compress backup
    print_status "Compressing backup..."
    gzip "$BACKUP_FILE"
    BACKUP_FILE="${BACKUP_FILE}.gz"
    
    print_success "Backup compressed: $BACKUP_FILE"
    
    # Display backup size
    BACKUP_SIZE=$(du -h "$BACKUP_FILE" | cut -f1)
    print_status "Backup size: $BACKUP_SIZE"
    
else
    print_error "Backup failed!"
    exit 1
fi

# Clean up old backups
print_status "Cleaning up old backups (keeping last $RETENTION_DAYS days)..."
find "$BACKUP_DIR" -name "clientback_backup_${ENVIRONMENT}_*.sql.gz" -type f -mtime +$RETENTION_DAYS -delete

REMAINING_BACKUPS=$(find "$BACKUP_DIR" -name "clientback_backup_${ENVIRONMENT}_*.sql.gz" -type f | wc -l)
print_success "Cleanup completed. $REMAINING_BACKUPS backup(s) remaining."

echo ""
echo "=============================================="
print_success "Database backup completed successfully!"
echo ""
echo "To restore this backup:"
echo "1. Stop the application:"
echo "   docker-compose -f $COMPOSE_FILE down app"
echo ""
echo "2. Restore the database:"
echo "   gunzip -c $BACKUP_FILE | docker-compose -f $COMPOSE_FILE exec -T db psql -U $DATABASE_USERNAME -d ${POSTGRES_DB:-$(basename "$DATABASE_URL")}"
echo ""
echo "3. Start the application:"
echo "   docker-compose -f $COMPOSE_FILE up -d"
