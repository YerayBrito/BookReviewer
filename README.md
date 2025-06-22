# Book Reviewer

A web application for reviewing and rating books, built with Quarkus (backend) and Vue.js + Quasar (frontend).

## Quick Start

### Prerequisites
- Java 21 or higher
- Node.js 18 or higher
- MariaDB (optional, H2 is used by default in development)

### Environment Setup

#### Backend
```bash
cd backend
# Copy environment example and configure if needed
cp env.example .env
# Run with default configuration
./mvnw quarkus:dev
```
Available at: `http://localhost:8080` (configurable via environment variables)

#### Frontend
```bash
cd frontend
# Copy environment example and configure if needed
cp env.example .env
npm install
npm run dev
```
Available at: `http://localhost:5173` (configurable via environment variables)

## Configuration

### Environment Variables

#### Backend (.env)
- `SERVER_PORT` - Server port (default: 8080)
- `DEV_SERVER_PORT` - Development server port (default: 8080)
- `TEST_SERVER_PORT` - Test server port (default: 8081)
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` - Database configuration
- `CORS_ORIGINS` - CORS allowed origins

#### Frontend (.env)
- `VITE_DEV_SERVER_PORT` - Development server port (default: 5173)
- `VITE_API_BASE_URL` - Backend API URL (default: http://localhost:8080/api)
- `VITE_APP_TITLE` - Application title

### Database Configuration
- **Development**: H2 in-memory database (default)
- **Production**: MariaDB (configure via environment variables)
- **Tests**: MariaDB with separate database

## Features
- Browse book catalog
- View book details and reviews
- Write and rate book reviews
- User profiles with review history

## API Endpoints
- Books: `/api/books`
- Reviews: `/api/reviews`
- Users: `/api/users` 