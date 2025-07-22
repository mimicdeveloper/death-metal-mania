# 🎸 Death Metal Mania

Welcome to **Death Metal Mania**, a full-stack web application for discovering death metal bands, viewing events, saving favorites, and managing content as a user or admin.

## 🔧 Setup Instructions

### 1. Acquire a Spotify API Token

You’ll need a Spotify API token to query band and album data.

- Visit the [Spotify Developer Dashboard](https://developer.spotify.com/dashboard/)
- Create an app to get your **Client ID** and **Client Secret**
- Use [Postman](https://www.postman.com/) or cURL to retrieve an access token:

- Copy the `access_token`. Note: It expires in 1 hour.
- Alternatively, the developer can provide you with a token.

### 2. Configure and Run the Backend

#### 2.1 Open the Backend Project
- Navigate to: `final-project/server/death-metal-mania-backend-final-project - Gold/`
- Open the project in **IntelliJ**
- Ensure that dependencies are loaded from `pom.xml`

#### 2.2 Update the Spotify Token
- Open: `src/main/resources/application.properties`
- Replace the token value with your current Spotify token:


#### 2.3 Run the Spring Boot Application
- Run the main application class from IntelliJ
- Leave the server running for the frontend to connect

### 3. Set Up the PostgreSQL Database

#### 3.1 Create a Database
- Open **pgAdmin**
- Create a database named: `deathmetalmania`

#### 3.2 Import Schema
- Open the file: `server/database/m2_final_project.sql`
- Copy the entire contents
- In pgAdmin, right-click the `deathmetalmania` database → Query Tool
- Paste the copied schema into the query editor and run it

### 4. Run the Frontend Client

#### 4.1 Open the Client Project
- Navigate to: `final-project/client/`
- Open the folder in **VS Code**

#### 4.2 Install Dependencies
- If not already installed, open terminal and run:

npm install

#### 4.3 Launch the Development Server
- In the terminal, run the following command to start the frontend development server:

npm run dev

#### 4.4 Open the Application in the Browser
- After the server starts, a local development URL will be displayed in the terminal, typically:

http://localhost:5173/

- Hold Ctrl and click the link to open the Death Metal Mania app in your default web browser.

## 👥 User Access Instructions

### Register a New Account
- You can register a new user account to explore the application.
- Registered users can access search, profile, and favorites functionality.

### Demo Credentials (Pre-Seeded)
Use the following credentials to log in without registering:

| Role  | Username | Password |
|-------|----------|----------|
| User  | `user`   | `password` |
| Admin | `admin`  | `password` |

> 🔐 Regular users **cannot** access the admin dashboard.  
> ✅ Admins **can** access both user and admin dashboards.

---

## ✅ Core Features

### Anonymous Users
- Can access:
  - Homepage
  - Contact page
  - Login and Register views
- Can search for bands and events
- ❌ Cannot save or rate any content

### Authenticated Users (Users and Admins)
- Can access their **user dashboard** to:
  - View their profile
  - Update account info (first name, last name, email)
  - View and manage favorite bands
- Can add bands to favorites and rate them (1–5 stars)
- Can update or remove favorites

### Admin Users Only
- Can access the **admin dashboard**
- Can manage (create/update/delete):
  - Users
  - Bands
  - Events
- Can still access all user-level features (e.g., profile, favorites)

## 🎉 Final Notes

- ⏰ Make sure to **update the Spotify token hourly**
- 🖥️ Keep the **backend running** while using the frontend
- 🤘🔥 **Enjoy Death Metal Mania – Let the brutality begin!**
