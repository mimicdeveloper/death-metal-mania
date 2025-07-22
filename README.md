# 🎸 Death Metal Mania

**Death Metal Mania** is a full-stack web application for discovering death metal bands, viewing events, saving favorites, and managing content with user and admin roles.

---

## 🚀 Quick Start

The backend uses a PostgreSQL database hosted on [Koyeb](https://www.koyeb.com/), and the frontend is a separate client app. The Spotify API token and credentials needed to query Spotify data are **already configured** and managed by the backend, including automatic token refreshing.

---

## 📋 Prerequisites

- Java 17+ and Maven (for backend development)  
- Node.js and npm (for frontend development)  
- PostgreSQL client (pgAdmin, DBeaver, or psql) for initial DB setup  
- Access to the Koyeb PostgreSQL instance (connection details will be provided)

---

## 🔧 Backend Setup

### 1. Connect to Koyeb PostgreSQL Database

- Obtain your Koyeb PostgreSQL connection details (host, port, database name, username, password).  
- Use pgAdmin, DBeaver, or any SQL client to connect to your Koyeb database.

### 2. Initialize Database Schema

- Open the SQL file located at: `server/database/m2_final_project.sql`  
- Run the entire SQL script against your Koyeb PostgreSQL database.

> **Important:** This step creates all necessary tables, constraints, and initial data.  
> The backend **does not automatically create or migrate** the database schema.

### 3. Configure and Run Backend

- Open the backend project located at `final-project/server/death-metal-mania-backend-final-project - Gold/` in IntelliJ.  
- Ensure `application.properties` is configured to point to the Koyeb PostgreSQL database.  
- Run the Spring Boot application main class.  
- Keep the backend running while using the frontend.

---

## 🌐 Frontend Setup

### 1. Open Frontend Project

- Navigate to `final-project/client/`  
- Open the folder in VS Code or your preferred editor.

### 2. Install Dependencies

```bash
npm install
3. Run Development Server
bash
Copy
Edit
npm run dev
Open your browser to the URL shown in the terminal (usually http://localhost:5173).

👤 User Access
Users can register and log in.

Authenticated users can search bands and events, save favorites, rate bands, and update their profile.

Admins have additional access to an admin dashboard for managing users, bands, and events.

⚙️ Key Features
For Anonymous Users
Browse homepage, contact page, login, and register.

Search for bands and events.

Cannot save or rate content.

For Authenticated Users
Manage user profile.

Add, update, and delete favorite bands with ratings.

Access favorites dashboard.

For Admin Users
Manage users, bands, and events via the admin dashboard.

Full CRUD capabilities on content.

🔄 Spotify Token Handling
Spotify API credentials and access tokens are managed internally by the backend.

The backend automatically refreshes the Spotify access token as needed.

No manual token acquisition or updates are required by users.

🗒️ Notes
Make sure the backend is always running and connected to the PostgreSQL database when using the frontend.

The PostgreSQL schema must be initialized manually once after creating the database on Koyeb.

You do not need to obtain your own Spotify API credentials.

🎸 Enjoy Death Metal Mania — Let the brutality begin! 🤘🔥
