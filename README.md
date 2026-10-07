# Feature Flag System

## What Is This?

A feature flag management system that lets you turn features on or off without redeploying your application.

Imagine you're running a website and you want to show a new feature to only some users — maybe 10% of visitors, or just your internal team. You don't want to redeploy your entire app every time you want to turn something on or off. That's what a feature flag does.

A feature flag is like a light switch for your software. You can turn features on or off instantly, without touching any code.

---

## How It Works

```
┌─────────────────────────────────────────────────────────┐
│  1. You create a flag via API                           │
│     POST /api/admin/flags { "key": "dark-mode" }         │
│  ↓                                                       │
│  2. Flag server stores it in PostgreSQL                 │
│  ↓                                                       │
│  3. Your app uses SDK to check flag status               │
│     if (sdk.isEnabled("dark-mode")) { show dark mode }   │
│  ↓                                                       │
│  4. SDK polls server every 30s for updates               │
│  ↓                                                       │
│  5. You toggle flag ON/OFF via API                       │
│     PUT /api/admin/flags/1/toggle                        │
│  ↓                                                       │
│  6. Your app sees the change within 30s                  │
└─────────────────────────────────────────────────────────┘
```

---

## Tech Stack

| Component | Technology |
|---|---|
| Backend | Spring Boot 4.1.1 (Java 21) |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Auth | JWT (JSON Web Tokens) |
| SDK | Java 21 |
| Build Tool | Gradle |

---

## Project Structure

```
feature-flag/
├── flag-server/          # Spring Boot server
│   ├── src/main/java/
│   │   └── com/zyop/featureFlag/
│   │       ├── controller/    # REST endpoints
│   │       ├── dto/           # Request/response objects
│   │       ├── exception/     # Error handling
│   │       ├── security/      # JWT utilities
│   │       ├── service/       # Business logic
│   │       └── config/        # Security config
│   └── src/main/resources/
│       ├── application.properties
│       └── db/migration/      # Flyway migrations
├── flag-sdk/             # Java SDK for client apps
│   └── src/main/java/
│       └── com/featureflag/sdk/
│           ├── FeatureFlagClient.java
│           └── FlagPoller.java
└── test-client/          # Demo app showing SDK usage
```

---

## API Endpoints

### Authentication

| Method | Endpoint | Auth | Purpose |
|---|---|---|---|
| POST | `/api/auth/login` | None | Get JWT token |

**Request:**
```json
{
  "username": "admin",
  "password": "changeme"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### SDK Endpoints (No Auth Required)

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/sdk/flags` | Get all flags |
| POST | `/api/sdk/evaluate` | Evaluate flag for user |

**Evaluate Request:**
```json
{
  "flagKey": "dark-mode",
  "userId": "user123",
  "group": "beta-testers"
}
```

**Evaluate Response:**
```json
{
  "flagKey": "dark-mode",
  "enabled": true,
  "reason": "Percentage rollout: 50%"
}
```

---

### Admin Endpoints (JWT Required)

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/admin/flags` | Get all flags |
| POST | `/api/admin/flags` | Create a flag |
| GET | `/api/admin/flags/{id}` | Get flag by ID |
| PUT | `/api/admin/flags/{id}/toggle` | Toggle flag |
| PATCH | `/api/admin/flags/{id}` | Update flag |
| DELETE | `/api/admin/flags/{id}` | Delete flag (soft-delete) |
| GET | `/api/admin/audit` | Get all audit history |
| GET | `/api/admin/audit/{flagId}` | Get audit history for flag |

**Create Flag Request:**
```json
{
  "key": "dark-mode",
  "name": "Dark Mode",
  "enabled": true
}
```

---

## Features

| Feature | Description |
|---|---|
| **Flag Management** | Create, read, update, delete (soft-delete) feature flags |
| **Toggle Switches** | Instantly enable/disable flags via API |
| **Metadata** | Track description, creation date, last modified date |
| **Validation** | Input validation with clear error messages |
| **Error Handling** | Structured error responses (400/404/409/500) |
| **Percentage Rollout** | Show feature to a percentage of users |
| **User Targeting** | Target specific users or groups with flags |
| **Server-side Evaluation** | Evaluate flags on the server with user context |
| **Audit Logging** | Track who changed what and when |
| **PostgreSQL** | Database with Flyway migrations |
| **JWT Authentication** | Token-based auth for admin endpoints |
| **SDK** | Java SDK for easy integration into your apps |
| **Polling** | SDK polls server for flag updates (30s interval) |
| **Fallback** | SDK keeps last known values if server is down |

---

## Getting Started

### Prerequisites

- Java 21
- Docker (for PostgreSQL)
- Gradle

### Setup

1. **Start PostgreSQL:**
   ```bash
   docker run --name feature-flag-db -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=featureflag -p 5432:5432 -d postgres:16
   ```

2. **Run the server:**
   ```bash
   ./gradlew :flag-server:bootRun
   ```

3. **Get a JWT token:**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "changeme"}'
   ```

4. **Create a flag:**
   ```bash
   curl -X POST http://localhost:8080/api/admin/flags \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     -d '{"key": "dark-mode", "name": "Dark Mode", "enabled": true}'
   ```

5. **Check flags:**
   ```bash
   curl http://localhost:8080/api/sdk/flags
   ```

---

## Deployment

### Using Neon (PostgreSQL) + Render (App)

#### Step 1: Set Up Neon

1. Go to [neon.tech](https://neon.tech)
2. Sign up with GitHub (no credit card required)
3. Create a new project
4. Get connection details (host, database, username, password)

#### Step 2: Configure Environment Variables

In Render, set these environment variables:

```
DB_HOST=your-neon-host
DB_PORT=5432
DB_NAME=your-neon-database
DB_USER=your-neon-username
DB_PASSWORD=your-neon-password
DB_SSLMODE=require
ADMIN_USERNAME=admin
ADMIN_PASSWORD=your-secure-password
```

#### Step 3: Deploy to Render

1. Push your code to GitHub
2. Sign up at [render.com](https://render.com)
3. Create a new Web Service
4. Connect your GitHub repo
5. Set Runtime to Docker
6. Add environment variables
7. Deploy

---

## Environment Variables

| Variable | Description | Default |
|---|---|---|
| `DB_HOST` | PostgreSQL host | `localhost` |
| `DB_PORT` | PostgreSQL port | `5432` |
| `DB_NAME` | Database name | `featureflag` |
| `DB_USER` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |
| `DB_SSLMODE` | SSL mode | `disable` |
| `ADMIN_USERNAME` | Admin username | `admin` |
| `ADMIN_PASSWORD` | Admin password | `changeme` |

---

## Future Roadmap

- **Percentage rollout** — Roll out features to a percentage of users (e.g., 10% of traffic)
- **User targeting** — Target specific users or groups with flags
- **Server-side evaluation** — Evaluate flags on the server with user context
- **Audit logging** — Track who changed what and when
- **Real-time updates** — Push flag changes instantly via SSE/WebSocket
- **Multi-tenancy** — Support multiple organizations with isolated flags
- **A/B testing** — Built-in A/B testing with metrics
- **Analytics dashboard** — Visualize flag usage and rollout metrics

---

## License

MIT
