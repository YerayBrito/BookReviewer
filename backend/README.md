# Backend - Book Reviewer

Backend for the Book Reviewer application built with Quarkus, Hibernate and Liquibase.

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.8+ (or use the included Maven wrapper)

### Run in Development
```bash
cd backend
./mvnw quarkus:dev
```

Available at: `http://localhost:8080`

### Verify
Open: `http://localhost:8080/api/books`

## Configuration

### Environment Variables
The application uses environment variables for dynamic configuration:

- `QUARKUS_HTTP_PORT`: Server port (default: 8080)
- `DB_HOST`: Database host (default: localhost)
- `DB_PORT`: Database port (default: 3306)
- `DB_NAME`: Database name (default: bookreviewer)
- `DB_USER`: Database username (default: root)
- `DB_PASSWORD`: Database password

### Custom Port Configuration
To run on a different port:
```bash
export QUARKUS_HTTP_PORT=9090
./mvnw quarkus:dev
```

Or create a `.env` file based on `env.example`:
```bash
cp env.example .env
# Edit .env with your configuration
./mvnw quarkus:dev
```

## Technologies
- Quarkus 3.11.0
- Hibernate ORM + Panache
- Liquibase
- H2 Database (development)
- MariaDB (production)
- Java 21

## API Endpoints
- Books: `/api/books`
- Reviews: `/api/reviews`
- Users: `/api/users`

## Database
- H2 in-memory for development
- MariaDB for production
- Automatic migrations with Liquibase
- Sample data included 