# 📸 CarSpotter

**CarSpotter** is a social media app for car enthusiasts to share, discover, and engage with rare and exotic cars spotted in the wild. Built with Android (Kotlin) and powered by a custom Ktor backend, the app creates a vibrant community for automotive lovers to connect and compete.

## 🚗 Features

- 📷 **Spot & Share Supercars** – Capture photos of unique cars and share them with the community.
- 💬 **Engage** – Like, comment, and interact with other users' car posts.
- 🏆 **Weekly Leaderboard** – Climb the ranks based on your car spotting activity. Points reset every week.
- 👥 **Social Features** – Follow users, send friend requests, and chat directly within the app.
- 🔐 **Authentication** – Email/password login with JWT-based security and Google Sign-In support.
- 📂 **Backend Integration** – Custom Ktor server with PostgreSQL, Exposed ORM, and Docker support for development and testing.

## 🛠 Tech Stack

### Android Client
- Kotlin + Jetpack Compose
- ViewModel, State Management
- Navigation Compose
- Material UI
- Custom JSON serializers

### Backend (Ktor Server)
- Ktor (REST API)
- PostgreSQL + Exposed ORM
- JWT Authentication
- Modular architecture (DAO, DTO, Repository layers)
- Dockerized testing environment
