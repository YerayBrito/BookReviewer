# BookReviewer

[![DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/YerayBrito/BookReviewer)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Contributors](https://img.shields.io/github/contributors/YerayBrito/BookReviewer?style=flat-square)](https://github.com/YerayBrito/BookReviewer/graphs/contributors)
[![Last Commit](https://img.shields.io/github/last-commit/YerayBrito/BookReviewer)](https://github.com/YerayBrito/BookReviewer/commits/develop)
[![Open Issues](https://img.shields.io/github/issues/YerayBrito/BookReviewer?style=flat-square)](https://github.com/YerayBrito/BookReviewer/issues)

A modern web application to review and rate books, built with **Quarkus** (backend) and **Vue.js + Quasar** (frontend).


## ✨ Features

- 📚 **Book Catalog:** Browse a curated list of books with cover images, authors, and descriptions.
- 🔍 **Book Details:** View detailed information for each book, including average rating and all user reviews.
- 📝 **Review System:** Write your own reviews and rate books from 1 to 5 stars.
- 👤 **User Profiles:** Each user has a profile page showing their review history and ratings.
- 📱 **Responsive Design:** Enjoy a seamless experience on desktop, tablet, and mobile devices.


## 🛠️ Technologies Used

- **Backend:** [Quarkus](https://quarkus.io/) (Java 21)
- **Frontend:** [Vue.js](https://vuejs.org/) + [Quasar Framework](https://quasar.dev/)
- **Database:** H2 (development), MariaDB (production/tests)
- **API:** RESTful endpoints
- **Testing:** JUnit (backend), Cypress (frontend)


## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Node.js 18 or higher
- MariaDB (optional, H2 is used by default for development)

### Installation

#### Backend

```bash
cd backend
cp env.example .env
./mvnw quarkus:dev
```
Backend will be running at: [http://localhost:8080](http://localhost:8080)

#### Frontend

```bash
cd frontend
cp env.example .env
npm install
npm run dev
```
Frontend will be running at: [http://localhost:5173](http://localhost:5173)


## ⚙️ Configuration

### Environment Variables

#### Backend (`.env`)
- `SERVER_PORT` - Server port (default: 8080)
- `DEV_SERVER_PORT` - Development server port (default: 8080)
- `TEST_SERVER_PORT` - Test server port (default: 8081)
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` - Database configuration
- `CORS_ORIGINS` - CORS allowed origins

#### Frontend (`.env`)
- `VITE_DEV_SERVER_PORT` - Development server port (default: 5173)
- `VITE_API_BASE_URL` - Backend API URL (default: http://localhost:8080/api)
- `VITE_APP_TITLE` - Application title

### Database

- **Development:** H2 in-memory database (default)
- **Production:** MariaDB (configure via environment variables)
- **Tests:** MariaDB with a separate database
