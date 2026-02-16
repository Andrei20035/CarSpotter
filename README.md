# 📸 CarSpotter

**CarSpotter** is a full-stack social media platform for car enthusiasts to discover, share, and compete through real-world supercar sightings.

Built with a modern Android client (Jetpack Compose) and a custom Ktor backend, the project demonstrates scalable architecture, secure authentication, cloud storage integration, and clean separation of concerns.

---

## 🧠 Architecture Overview

CarSpotter follows a modular, layered architecture designed for scalability and maintainability.

### System Components

- 📱 Android Client (Kotlin + Jetpack Compose)
- 🌐 REST API (Ktor)
- 🗄 PostgreSQL Database
- ☁️ AWS S3 (image storage)
- 🔐 JWT Authentication (Access + Refresh Tokens)
- 🐳 Docker (local development & testing)

---

## 🏗 Backend Architecture

The backend is structured into clear layers:

- **Routing Layer** – Defines HTTP endpoints
- **Repository Layer** – Business logic abstraction
- **DAO Layer** – Database operations
- **DTO Layer** – Safe data transfer models
- **Model Layer** – Core domain entities
- **Security Module** – JWT + Google OAuth integration

This separation ensures:

- Testability
- Clean responsibility boundaries
- Easier debugging
- Scalability for future features

---

## 📱 Android Architecture

The Android app follows modern best practices:

- **Kotlin**
- **Jetpack Compose**
- **MVVM Architecture**
- **Unidirectional Data Flow**
- **ViewModel + State Management**
- **Navigation Compose**
- **Material Design 3**
- Secure JWT storage using Android Keystore

---

## 🚗 Core Features

### 📷 Spot & Share
- Upload photos of rare or exotic cars
- Images stored securely in AWS S3
- Metadata stored in PostgreSQL

### 💬 Social Interactions
- Like posts
- Comment on sightings
- Send friend requests
- Private messaging between users

### 🏆 Weekly Leaderboard
- Points awarded based on activity
- Leaderboard resets weekly
- Competitive engagement system

### 🔐 Secure Authentication
- Email & password login
- JWT access + refresh token strategy
- Google Sign-In integration
- Protected routes using authentication middleware
- Password hashing and input validation

---

## 🛠 Tech Stack

### Android Client
- Kotlin
- Jetpack Compose
- MVVM
- Navigation Compose
- Material 3
- Custom JSON serialization

### Backend
- Ktor (REST API)
- PostgreSQL
- Exposed ORM
- JWT Authentication
- Google OAuth
- Dockerized development environment

### Cloud & Infrastructure
- AWS S3 (media storage)
- Environment-based configuration
- Dockerized PostgreSQL for local development

---

## 🔐 Security Considerations

- Access & Refresh token implementation
- Password hashing
- Request validation & sanitization
- Separation between internal models and public DTOs
- Authentication middleware for protected endpoints

---

## 🧪 Development & Testing

- Docker-based local environment
- Modular repository abstraction for easier testing
- DAO isolation for database testing
- Clean separation between data, business logic, and routing

---

## 🚀 What This Project Demonstrates

- Full-stack system design
- Clean architecture principles
- Secure authentication implementation
- REST API design best practices
- Database modeling & relationships
- Cloud storage integration
- Modern Android development (Compose)
- Scalable backend structure

---

## 📌 Future Improvements

- Real-time notifications (WebSockets)
- CI/CD pipeline
- Automated integration tests
- Admin moderation panel
- Caching layer for performance optimization

---

## 📄 License

This project is for portfolio and educational purposes.
