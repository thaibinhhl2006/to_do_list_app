# 📝 To-Do List
**To-Do List** is a RESTful API for task management built with **Spring Boot**, using **MySQL** as the database engine to store user and task data safely and reliably. The project integrates a full **JWT (JSON Web Token)** authentication flow — sign up, login, and refresh token — ensuring each user can only access and manage their own task list.

This is an open-source project, great for anyone who wants to learn how to build a complete backend using a layered architecture (Controller – Service – Repository) combined with Spring Security, and a solid foundation to extend with more advanced features in the future.

## 🚀 Key Features

- **JWT Authentication & Authorization**: Secure sign up, login, and refresh token flow powered by Spring Security
- **Task Management (CRUD)**: Create, view, update, and delete tasks scoped to each authenticated user
- **Task Status Tracking**: Manage task progress through the `TaskStatus` enum (e.g. TODO, IN_PROGRESS, DONE)
- **Persistent Storage**: Reliable data persistence and synchronization with MySQL via Spring Data JPA
- **Centralized Exception Handling**: Clear, consistent error responses through a global exception handler
- **Layered Architecture**: Clean separation between Controller, Service, and Repository layers

## 🛠️ Technologies Used

| Category | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security, JWT |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Build Tool | Maven |

## 📂 Folder Structure

```
to_do_list_app/
├── .idea/                              # IDE configuration (IntelliJ)
└── todolist/                           # Main application source code (Spring Boot)
    ├── .mvn/wrapper/                   # Maven wrapper
    ├── src/
    │   ├── main/
    │   │   ├── java/com/special/todolist/
    │   │   │   ├── config/             # Application configuration (database, beans, CORS...)
    │   │   │   ├── controller/         # REST Controllers (Auth, Task...)
    │   │   │   ├── domain/             # Core business objects
    │   │   │   ├── dto/                # Data Transfer Objects (request/response)
    │   │   │   ├── entity/             # Entities mapped to MySQL tables (Task, User...)
    │   │   │   ├── exception/          # Centralized exception handling (Global Exception Handler)
    │   │   │   ├── repository/         # Spring Data JPA Repositories
    │   │   │   ├── security/           # Spring Security & JWT configuration
    │   │   │   ├── service/            # Business logic
    │   │   │   └── TodolistApplication.java   # Application entry point
    │   │   └── resources/              # application.properties/yml, static resources
    │   └── test/java/com/special/todolist/
    │       └── TodolistApplicationTests.java
    ├── .env.example                     # Sample environment variables file (copy as .env)
    ├── .gitignore
    ├── mvnw / mvnw.cmd                  # Maven wrapper scripts
    └── pom.xml                          # Maven configuration & dependencies
```

## ⚙️ Prerequisites

Before you begin, make sure you have the following installed:

- **Java Development Kit (JDK)** 17 or higher
- **MySQL Server** 8.0 or higher
- **Maven** (or use the Maven Wrapper `mvnw` already included in the project — no separate install needed)
- **MySQL Workbench** or any MySQL admin tool (recommended)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code
- **Git** to clone the project

> 💡 *Note: Double-check and update the JDK version to match the actual configuration in your `pom.xml`.*

## 📦 Installation & Local Setup

**1. Clone the project**

```bash
git clone https://github.com/thaibinhhl2006/to_do_list_app.git
cd to_do_list_app/todolist
```

**2. Create the MySQL database**

Log in to MySQL and create a new database:

```sql
CREATE DATABASE todolist_db;
```

Spring Data JPA will automatically create the required tables on the first run (based on the `ddl-auto` configuration).

**3. Configure environment variables**

The project uses a `.env` file to store sensitive information (database connection, JWT secret, etc.). Copy the sample file and fill in your own values:

```bash
cp .env.example .env
```

Open the newly created `.env` file and update the values:

```dotenv
DB_URL=jdbc:mysql://localhost:3306/todolist_db
DB_USERNAME=root
DB_PASSWORD=your_password

JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=3600000
```

> ⚠️ The actual variable names may differ slightly — check the project's `.env.example` file for the exact keys.

**4. Build and run the application**

Use the Maven Wrapper included in the project (no need to install Maven separately):

```bash
# On macOS/Linux
./mvnw clean install
./mvnw spring-boot:run

# On Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

The application will run at `http://localhost:8080` (or the port configured in `application.properties`).

## 💡 Usage

Once the application is running, you first need to **sign up / log in** to obtain a JWT token, then use that token to call the task management APIs.

### Authentication

**Sign up for a new account**

```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "binh123",
  "email": "binh123@example.com",
  "password": "your_password"
}
```

**Log in**

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "binh123",
  "password": "your_password"
}
```

The response will contain an `accessToken` and a `refreshToken`:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Refresh the access token**

```http
POST /api/auth/refresh-token
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Task Management

For the requests below, attach the access token to the `Authorization` header:

```http
Authorization: Bearer <accessToken>
```

**Create a new task**

```http
POST /api/tasks
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "title": "Finish the weekly report",
  "description": "Compile the figures and send them to the manager",
  "status": "TODO"
}
```

**Get the current user's tasks**

```http
GET /api/tasks
Authorization: Bearer <accessToken>
```

**Update a task's status**

```http
PUT /api/tasks/{id}
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "status": "DONE"
}
```

**Delete a task**

```http
DELETE /api/tasks/{id}
Authorization: Bearer <accessToken>
```

> 📌 *Note: The routes and payload shapes above are illustrative, based on the project's architecture. Check the actual `Controller` and `DTO` classes in the source code (`controller/` and `dto/` folders) to confirm the exact contracts.*

## 🤝 Contributing

Contributions are always welcome! If you'd like to help improve this project, please follow these steps:

1. **Fork** this repository to your own account
2. Create a new branch for your feature or bug fix
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Commit your changes
   ```bash
   git commit -m "Add: ..."
   ```
4. Push the branch to your repository
   ```bash
   git push origin feature/your-feature-name
   ```
5. Open a **Pull Request** for review and merging

Found a bug or have an idea? Feel free to open an [Issue](https://github.com/thaibinhhl2006/to_do_list_app/issues).
<p align="center">
  Made with ❤️ by <a href="https://github.com/thaibinhhl2006">thaibinhhl2006</a>
</p>
